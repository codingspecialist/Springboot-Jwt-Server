package com.metacoding.restserver.util;

import com.metacoding.restserver._core.util.Resp;
import org.junit.jupiter.api.Test;

public class RespTest {

    @Test
    public void success_test(){
        Resp resp = Resp.success("안녕");
        System.out.println(resp);
    }
}
