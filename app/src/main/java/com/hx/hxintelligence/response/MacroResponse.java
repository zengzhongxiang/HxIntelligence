package com.hx.hxintelligence.response;

import java.util.List;

/**
 * 情景模式返回的数据
 */
public class MacroResponse extends BaseResponse {
    private String center_id;
    private List<MacroInfo> macros;  //返回的空调数据

    public String getCenter_id() {
        return center_id;
    }

    public void setCenter_id(String center_id) {
        this.center_id = center_id;
    }

    public List<MacroInfo> getMacros() {
        return macros;
    }

    public void setMacros(List<MacroInfo> macros) {
        this.macros = macros;
    }

    public class MacroInfo{
        private String macro_id;  //情景的id
        private String name;   //情景的名字

        public String getMacro_id() {
            return macro_id;
        }

        public void setMacro_id(String macro_id) {
            this.macro_id = macro_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
