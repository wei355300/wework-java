package com.qc.wework.chatdata;

import com.qc.utils.JsonUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.util.HashMap;
import java.util.Map;

public class JsonTest {

    public static void main(String[] args) {
        String s = "{\\\"msgid\\\":\\\"14667261219989978888_1614922904_external\\\",\\\"action\\\":\\\"send\\\",\\\"from\\\":\\\"wmxyF2EAAAEkuPrOKca1gYv08G0HGnUw\\\",\\\"tolist\\\":[\\\"HuaChao\\\",\\\"LiuLiXia\\\",\\\"luker\\\",\\\"ZhangTao\\\",\\\"krisGaoXianSen\\\",\\\"wmxyF2EAAAXYXAVEaHugTE0fUhrHQ9Vg\\\",\\\"wmxyF2EAAAQt6KraL8jOZ1cT7z8ixRdg\\\"],\\\"roomid\\\":\\\"wrxyF2EAAAA5MNzMFTrfalcc3Pkpe6Eg\\\",\\\"msgtime\\\":1614922903962,\\\"msgtype\\\":\\\"voice\\\",\\\"voice\\\":{\\\"md5sum\\\":\\\"8c7adc8978167321b7d5fc632df29028\\\",\\\"voice_size\\\":4234,\\\"play_length\\\":6,\\\"sdkfileid\\\":\\\"CtsDKjEqaDF0VmRKemZ4cmNWMHF0T1FyK2JYWmdZb3NkcG1WMEN3T3pOaGRUTHlNVk1pczB0b2F0SGZ6aWxPaVNBdU5pWFMyS2N4UU14MGdzUloyQWdnTWIxQUJiZWhTQ0xFcENYMnVrL2Z5bzhOVFp6dVRra3Y2YkdhQnYzRGdPRE5VYUcxTmh4cytpU2xscGJqc1JDdjZTRXNhazlGLzV3V0NUMTA3ZFp6V3lqZzVHNGpleGN6S3NHWnpsSm90cHREQUJQUzRRNVdsOUQzZDZGSUZqYWt4cGVpK2dNeE9uZWs0bkt3WklhMktidWNoMmxHK1kyYXZZb1dOUXF3b3RkYmFDWENxVXFoQ1NPYzg3a1ZDSVpQZG14SUgxMUlEZ3lIWXVnZThsTUdNVm5SMWtVQkgvck8rQytTK2pHeldNYXBILys1MktWN0VIeC92enNra0ttSGxSQTF4dVJmSkFWck9vZldka0hwQlBJbEY2WHhEelZxMGU2a0p4UmhWWEd2Q29qUE9WSzFkZWxEWEpETmZ5VG5kTVFYdm5FMmFzaDdHbFM0VEh1MGFaUndHeDhJNG52b2RMK0EvUHNZVlRSdHk5eTQ4eXRVYkhNa3ZWY3ZPZ05BWEh1QlE9PRI0TkRkZk56ZzRNVE13TXpJNU9EQTBNRGc1TVY4M05EVTROalF5TlY4eE5qRTBPVEl5T1RBMBogNjg3NTY3NmE3ODZmNjY2Nzc1NjY3OTY0Nzg3MTZlNmM=\\\"}}";
        s = StringEscapeUtils.unescapeJava(s);
        System.out.println(s);
        Map m = JsonUtils.parser(s, HashMap.class);

    }
}
