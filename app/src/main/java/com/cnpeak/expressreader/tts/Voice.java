package com.cnpeak.expressreader.tts;


import androidx.annotation.StringDef;

public class Voice {
    /**
     * 语言包
     */
    public String lang;
    /**
     * 语音播放声音
     */
    String voiceName;
    /**
     * 速率
     */
    String rate;
    /**
     * 性别
     */
    String gender;
    /**
     * 是否是云服务声音
     */
    private boolean isServiceVoice;

    @StringDef({"Female", "Male"})
    @interface Gender {

    }

    @StringDef({"zh-CN", "en-US"})
    @interface Lang {

    }


    public static class Builder {
        String lang;
        String voiceName;
        String gender;
        String rate;
        boolean isServiceVoice;

        public Builder() {
        }

        public Voice chineseVoice(@Gender String gender) {
            final Voice voice = new Voice();
            voice.lang = "zh-CN";
            voice.voiceName = "Microsoft Server Speech Text to Speech Voice (zh-CN, HuihuiRUS)";
            voice.gender = gender;
            voice.isServiceVoice = true;
            return voice;
        }

        Voice englishVoice(String gender) {
            final Voice voice = new Voice();
            voice.lang = "en-US";
            voice.voiceName = "Microsoft Server Speech Text to Speech Voice (en-US, ZiraRUS)";
            voice.gender = gender;
            voice.isServiceVoice = true;
            return voice;
        }

        public Builder setLang(@Lang String lang) {
            this.lang = lang;
            return this;
        }

        public Builder setVoiceName(String voiceName) {
            this.voiceName = voiceName;
            return this;
        }

        public Builder setGender(@Gender String gender) {
            this.gender = gender;
            return this;
        }

        public Builder setRate(String rate) {
            this.rate = rate;
            return this;
        }

        public Builder isServiceVoice(boolean serviceVoice) {
            isServiceVoice = serviceVoice;
            return this;
        }

        public Voice build() {
            final Voice voice = new Voice();
            voice.lang = this.lang;
            voice.voiceName = this.voiceName;
            voice.gender = this.gender;
            voice.rate = this.rate;
            voice.isServiceVoice = this.isServiceVoice;
            return voice;
        }
    }

    public String getLang() {
        return lang;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public String getGender() {
        return gender;
    }

    public Boolean getServiceVoice() {
        return isServiceVoice;
    }


}
