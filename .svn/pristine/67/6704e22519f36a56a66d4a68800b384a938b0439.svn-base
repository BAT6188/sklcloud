package com.skl.cloud.remote.ipc.dto.ipc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.skl.cloud.util.constants.Constants;
/**
  * @ClassName: AudioChannelIO
  * @Description: AudioChannel OXM bean
  * @author yangbin
  * @date 2015年10月14日
 */
@XmlRootElement(name = "audioChannel")
@XmlAccessorType(XmlAccessType.FIELD)
public class AudioChannelIO {
    // req 用来区分不同的通道,默认是AIC3101
    private String id = Constants.CHANNEL_ID_AIC3101;//default
    // req 通道打开或关闭
    private boolean enabled = true;//default
    // req 语音类型
    private String audioMode = AudioModeType.TALKORLISTEN.getValue();//default
    // opt 使能或禁止通道的麦克风
    private String microphoneEnabled;
    // opt 麦克风的音源
    private String microphoneSource;
    // opt 麦克风的音量
    private String microphoneVolume;
    // opt 使能或禁止通道的喇叭
    private String speakerEnabled;
    // opt 喇叭的音量
    private String speakerVolume;

    //setter/getter
    public String isMicrophoneEnabled() {
        return microphoneEnabled;
    }

    public void setMicrophoneEnabled(String microphoneEnabled) {
        this.microphoneEnabled = microphoneEnabled;
    }

    public String getMicrophoneSource() {
        return microphoneSource;
    }

    public void setMicrophoneSource(String microphoneSource) {
        this.microphoneSource = microphoneSource;
    }

    public String getMicrophoneVolume() {
        return microphoneVolume;
    }

    public void setMicrophoneVolume(String microphoneVolume) {
        this.microphoneVolume = microphoneVolume;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAudioMode() {
        return audioMode;
    }

    public void setAudioMode(String audioMode) {
        this.audioMode = audioMode;
    }

    public String isSpeakerEnabled() {
        return speakerEnabled;
    }

    public void setSpeakerEnabled(String speakerEnabled) {
        this.speakerEnabled = speakerEnabled;
    }

    public String getSpeakerVolume() {
        return speakerVolume;
    }

    public void setSpeakerVolume(String speakerVolume) {
        this.speakerVolume = speakerVolume;
    }

    // AudioMode的常量值
    public enum AudioModeType {
        LISTENONLY("listenonly"), TALKONLY("talkonly"), TALKORLISTEN("talkorlisten"), TALKANDLISTEN("talkandlisten");

        private String value;

        public String getValue() {
            return value;
        }

        private AudioModeType(String value) {
            this.value = value;
        }
    }

}
