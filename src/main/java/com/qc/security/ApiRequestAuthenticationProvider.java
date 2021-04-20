package com.qc.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

public class ApiRequestAuthenticationProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {

    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";
    protected final Log logger = LogFactory.getLog(getClass());
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    protected boolean hideUserNotFoundExceptions = true;
    private UserCache userCache = new NullUserCache();
    private boolean forcePrincipalAsString = false;
    private UserDetailsChecker preAuthenticationChecks = new DefaultPreAuthenticationChecks();
    private UserDetailsChecker postAuthenticationChecks = new DefaultPostAuthenticationChecks();
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private PasswordEncoder passwordEncoder;

    private volatile String userNotFoundEncodedPassword;

    private UserDetailsService userDetailsService;

    private UserDetailsPasswordService userDetailsPasswordService;


    public ApiRequestAuthenticationProvider() {
        setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    public ApiRequestAuthenticationProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public final void afterPropertiesSet() throws Exception {
        Assert.notNull(this.userCache, "A account cache must be set");
        Assert.notNull(this.messages, "A message source must be set");
        doAfterPropertiesSet();
    }

    @Override
    public Authentication authenticate(Authentication authentication2) throws AuthenticationException {
        Assert.isInstanceOf(ApiRequestAuthenticationToken.class, authentication2,
                () -> this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports",
                        "Only ApiRequestAuthenticationToken is supported"));

        ApiRequestAuthenticationToken apiRequestAuthentication = (ApiRequestAuthenticationToken) authentication2;
        String token = determineToken(apiRequestAuthentication);
        boolean cacheWasUsed = true;
        UserDetails user = this.userCache.getUserFromCache(token);
        if (user == null) {
            cacheWasUsed = false;
            try {
                user = retrieveUser(token, apiRequestAuthentication);
            } catch (UsernameNotFoundException ex) {
                this.logger.debug("Failed to find token: '" + token + "'");
                if (!this.hideUserNotFoundExceptions) {
                    throw ex;
                }
                throw new BadCredentialsException(this.messages
                        .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
            }
            Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");
        }
        try {
            this.preAuthenticationChecks.check(user);
            additionalAuthenticationChecks(user, apiRequestAuthentication);
        } catch (AuthenticationException ex) {
            if (!cacheWasUsed) {
                throw ex;
            }
            // There was a problem, so try again after checking
            // we're using latest data (i.e. not from the cache)
            cacheWasUsed = false;
            user = retrieveUser(token, apiRequestAuthentication);
            this.preAuthenticationChecks.check(user);
            additionalAuthenticationChecks(user, apiRequestAuthentication);
        }
        this.postAuthenticationChecks.check(user);
        if (!cacheWasUsed) {
            this.userCache.putUserInCache(user);
        }

        return createSuccessAuthentication(apiRequestAuthentication.getToken(), apiRequestAuthentication, user);
    }

    private String determineToken(ApiRequestAuthenticationToken authentication) {
        return authentication.getToken();
    }

    protected Authentication createSuccessAuthentication(String principal, ApiRequestAuthenticationToken authentication,
                                                         UserDetails user) {
        boolean upgradeEncoding = this.userDetailsPasswordService != null
                && this.passwordEncoder.upgradeEncoding(user.getPassword());
        if (upgradeEncoding) {
            String presentedPassword = authentication.getCredentials().toString();
            String newPassword = this.passwordEncoder.encode(presentedPassword);
            user = this.userDetailsPasswordService.updatePassword(user, newPassword);
        }
        // Ensure we return the original credentials the account supplied,
        // so subsequent attempts are successful even with encoded passwords.
        // Also ensure we return the original getDetails(), so that future
        // authentication events after cache expiry contain the details
//        ApiRequestAuthenticationToken result = new ApiRequestAuthenticationToken(principal, this.authoritiesMapper.mapAuthorities(account.getAuthorities()));
//        result.setDetails(authentication.getDetails());
        this.logger.debug("Authenticated account");
        return authentication;
    }

    protected void doAfterPropertiesSet() {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
    }

    protected final UserDetails retrieveUser(String token, ApiRequestAuthenticationToken authentication)
            throws AuthenticationException {
        prepareTimingAttackProtection();
        try {
            UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(token);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        } catch (UsernameNotFoundException ex) {
            mitigateAgainstTimingAttack(authentication);
            throw ex;
        } catch (InternalAuthenticationServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    @SuppressWarnings("deprecation")
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  ApiRequestAuthenticationToken authentication) throws AuthenticationException {
//        if (authentication.getToken() == null) {
//            this.logger.debug("Failed to authenticate since no credentials provided");
//            throw new BadCredentialsException(this.messages
//                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
//        }
//        String presentedPassword = authentication.getCredentials().toString();
//        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
//            this.logger.debug("Failed to authenticate since password does not match stored value");
//            throw new BadCredentialsException(this.messages
//                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
//        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (ApiRequestAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
        }
    }

    private void mitigateAgainstTimingAttack(ApiRequestAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }
    }

    protected PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
        this.userNotFoundEncodedPassword = null;
    }

    protected UserDetailsService getUserDetailsService() {
        return this.userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {

        @Override
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                ApiRequestAuthenticationProvider.this.logger
                        .debug("Failed to authenticate since account account is locked");
                throw new LockedException(ApiRequestAuthenticationProvider.this.messages
                        .getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked"));
            }
            if (!user.isEnabled()) {
                ApiRequestAuthenticationProvider.this.logger
                        .debug("Failed to authenticate since account account is disabled");
                throw new DisabledException(ApiRequestAuthenticationProvider.this.messages
                        .getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
            }
            if (!user.isAccountNonExpired()) {
                ApiRequestAuthenticationProvider.this.logger
                        .debug("Failed to authenticate since account account has expired");
                throw new AccountExpiredException(ApiRequestAuthenticationProvider.this.messages
                        .getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"));
            }
        }

    }

    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {

        @Override
        public void check(UserDetails user) {
            if (!user.isCredentialsNonExpired()) {
                ApiRequestAuthenticationProvider.this.logger
                        .debug("Failed to authenticate since account account credentials have expired");
                throw new CredentialsExpiredException(ApiRequestAuthenticationProvider.this.messages
                        .getMessage("AbstractUserDetailsAuthenticationProvider.credentialsExpired",
                                "User credentials have expired"));
            }
        }

    }

}
