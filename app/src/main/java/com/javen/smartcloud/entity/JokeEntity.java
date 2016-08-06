package com.javen.smartcloud.entity;

import java.util.List;

/**
 * Created by Javen on 16/8/6.
 */
public class JokeEntity {


    /**
     * error_code : 0
     * reason : Success
     * result : {"data":[{"content":"昨天打车出门，路上，遇到两个女的打架，都动上手了。司机师傅突然问我：你赶时间吗？我说：不呀，怎么了？然后司机师傅靠边停车，指着打架的地方：不着急，我们看会再走，我觉得快要扒衣服了！","hashId":"76e5f77d8a15d82a6a07b183ec6852c3","unixtime":1470475431,"updatetime":"2016-08-06 17:23:51"},{"content":"士兵：\u201c将军，我们中出了一个女叛徒。\u201d&nbsp;    将军：\u201c你们怎么处置的？\u201d&nbsp;    士兵：\u201c啊...我不是已经说了吗？\u201d","hashId":"c4e3e428c1eacbb4ed284bad3a15ffcd","unixtime":1470452632,"updatetime":"2016-08-06 11:03:52"}]}
     */

    private int error_code;
    private String reason;
    private Data result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Data getResult() {
        return result;
    }

    public void setResult(Data result) {
        this.result = result;
    }

    public static class Data {
        /**
         * content : 昨天打车出门，路上，遇到两个女的打架，都动上手了。司机师傅突然问我：你赶时间吗？我说：不呀，怎么了？然后司机师傅靠边停车，指着打架的地方：不着急，我们看会再走，我觉得快要扒衣服了！
         * hashId : 76e5f77d8a15d82a6a07b183ec6852c3
         * unixtime : 1470475431
         * updatetime : 2016-08-06 17:23:51
         */

        private List<Subject> data;

        public List<Subject> getData() {
            return data;
        }

        public void setData(List<Subject> data) {
            this.data = data;
        }

        public static class Subject {
            private String content;
            private String hashId;
            private long unixtime;
            private String updatetime;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getHashId() {
                return hashId;
            }

            public void setHashId(String hashId) {
                this.hashId = hashId;
            }

            public long getUnixtime() {
                return unixtime;
            }

            public void setUnixtime(long unixtime) {
                this.unixtime = unixtime;
            }

            public String getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
            }

            @Override
            public String toString() {
                return "Subject{" +
                        "content='" + content + '\'' +
                        ", hashId='" + hashId + '\'' +
                        ", unixtime=" + unixtime +
                        ", updatetime='" + updatetime + '\'' +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "JokeEntity{" +
                "error_code=" + error_code +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }
}
