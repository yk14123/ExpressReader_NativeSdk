    package com.cnpeak.expressreader.model.bean;

    import androidx.annotation.NonNull;

    import java.io.Serializable;
    import java.util.List;

    /**
     * @author builder by HUANG JIN on ${date}
     * @version 1.0
     * @description 单本杂志bean
     */
    public class IssueLite implements Serializable {

        /**
         * MgId : C0120180801N6708
         * MgName : 今日中国
         * IssuerName : 今日中国杂志社
         * IssueNo : 6708
         * PublishDate : 2018-07-31
         * Cover : https://magexp.oss-cn-beijing.aliyuncs.com/2052/C0120180801N6708/jpeg/001.jpg
         * BType : L
         * Units : [{"MnId":"28B130FD-5755-4F30-8A80-407BFF18E392","Title":"中国与世界：分享与成长","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533777062445.jpg"],"Videos":"[]"},{"MnId":"4BC587E6-F447-41AB-AF61-645E61BBAD51","Title":"资讯","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533777348435.jpg"],"Videos":"[]"},{"MnId":"704F0220-A1E4-4AC3-84A8-CD82D04CD706","Title":"金砖合作再出发","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533777398681.jpg"],"Videos":"[]"},{"MnId":"AD568F05-E137-4E36-A00B-AAD2047FC099","Title":"金砖约堡会晤：为共同繁荣而合作","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533777476664.jpg"],"Videos":"[]"},{"MnId":"C97D4788-F9B1-4BD3-AF7A-EB2A28AFB7E9","Title":"首届中国国际进口博览会筹备工作进入\u201c最后冲刺\u201d阶段","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533777684876.jpg"],"Videos":"[]"},{"MnId":"CB1AFE6B-CDB5-4782-B627-CC36ECDCFA4E","Title":"开放的中国与世界共赢","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533777820024.jpg"],"Videos":"[]"},{"MnId":"1419EBFF-C436-4F3D-9CEA-0A59120F786B","Title":"吉利：\u201c跑\u201d遍全世界","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533778085211.jpg"],"Videos":"[]"},{"MnId":"B1520377-18E6-4E80-98B4-448EBFAA459C","Title":"阿里巴巴：见证开放的中国","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533778405735.jpg"],"Videos":"[]"},{"MnId":"61168CF5-5806-41AD-95F6-53922C76A365","Title":"扩大进口：中国开放的世界红利","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533778621447.jpg"],"Videos":"[]"},{"MnId":"85A97451-6708-494B-BB5F-AD752C659CEC","Title":"马凯硕：世界从中国的发展中获益","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533778818801.jpg"],"Videos":"[]"},{"MnId":"037E5453-B069-4BCF-A155-5556BDB52170","Title":"时事虽艰 前景光明\u2014中国与WTO的未来","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533778970178.jpg"],"Videos":"[]"},{"MnId":"35D7658A-83B1-4383-A6A3-5D92C2545FE4","Title":"三下南洋，亲历改革开放","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533778593442.jpg"],"Videos":"[]"},{"MnId":"763EE484-B75E-4780-89C1-B5C80BC7BCC2","Title":"开放之桥梁：中国与WTO的\u201c世纪情缘\u201d","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533778735644.jpg"],"Videos":"[]"},{"MnId":"9D1D7AE8-2410-4C0C-8F86-814DD9931E6C","Title":"准确把握时代大势，共同推进人类进步","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533778898317.jpg"],"Videos":"[]"},{"MnId":"64A7E1EC-B7CF-4C4D-9C98-835B2D7D9541","Title":"中欧合作：为世界注入稳定性和正能量","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533779041023.jpg"],"Videos":"[]"},{"MnId":"4FDDBE35-45FA-4564-AF81-325662FB166B","Title":"科技打造服务为民新模式","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533779225542.jpg"],"Videos":"[]"},{"MnId":"EB485814-D93B-474A-B1C2-74F3F1C3CD55","Title":"以敬民之心行简政之道","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533779341769.jpg"],"Videos":"[]"},{"MnId":"5D0F82D4-3385-4BB0-93EC-62C93F8ED935","Title":"慧治亦庄 服务为民","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533779509876.jpg"],"Videos":"[]"},{"MnId":"613E54C0-142B-408C-9FA5-2773D5362A12","Title":"众筹事件引发的争论","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533779560344.jpg"],"Videos":"[]"},{"MnId":"3D8B6CA6-FF5D-41A0-B035-A53E88E52BD1","Title":"微科技","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533779669427.jpg"],"Videos":"[]"},{"MnId":"572AAC07-97C3-47B2-BC45-CDDFF86D2D0C","Title":"舒畅：造火箭的85后","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533779868888.jpg"],"Videos":"[]"},{"MnId":"987232DD-2855-4A0F-9125-04BFBAAD1459","Title":"\u201c美\u201d离我们到底有多远","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533779959053.jpg"],"Videos":"[]"},{"MnId":"D3D7DFC8-C136-441E-BB23-8C65EC19F5FB","Title":"生态优先 绿色发展","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533780079290.jpg"],"Videos":"[]"},{"MnId":"AF4EA2BA-AEA4-4C3B-85A3-016EAC7B85F7","Title":"\u201c一带一路\u201d促进绿色发展","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533780191770.jpg"],"Videos":"[]"},{"MnId":"B7EF11F7-C6BC-4E2C-A917-E2BD8A67E917","Title":"张彦：值得永远学习的师长","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533777006443.jpg"],"Videos":"[]"},{"MnId":"AB9E860D-0BC7-4552-B4CC-78027489D029","Title":"余秀华：人生辽阔值得轻言细语","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533777260231.jpg"],"Videos":"[]"},{"MnId":"1649EACB-04BA-48FD-BF78-30FD3ABA4607","Title":"上海国际电影节：百年缘分新起点","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533777644996.jpg"],"Videos":"[]"},{"MnId":"FDFC3BAF-3A42-47DA-B2E9-180A877E5704","Title":"中国二手书店的商业探索","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533778006739.jpg"],"Videos":"[]"},{"MnId":"755817EE-646D-41C0-A4E6-EED1F66E0203","Title":"新加坡\u201c新生代\u201d的华文教育","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533778377874.jpg"],"Videos":"[]"},{"MnId":"FEA33359-09CE-4AB2-83F2-D3292D4EDFA9","Title":"惠台\u201c31条\u201d促两岸融合发展","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533778731143.jpg"],"Videos":"[]"},{"MnId":"D66CD5DF-AD1B-4A50-8A36-8233F62CD900","Title":"热点","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533778850442.jpg"],"Videos":"[]"},{"MnId":"2791313E-339C-46E4-AB1C-301D091BD4AF","Title":"乡村振兴之路的江西特色","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533779101849.jpg"],"Videos":"[]"},{"MnId":"BCF158D3-66C0-423D-A3E9-B397854B9F27","Title":"世界杯\u201c进化史\u201d","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533779414763.jpg"],"Videos":"[]"},{"MnId":"3A7D68EC-F54C-4162-91EF-BFC56915EBC0","Title":"雄秀北岳 人文之山","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533779581048.jpg"],"Videos":"[]"},{"MnId":"5773EABF-F9A7-4A90-BAB5-86376EE9CCD8","Title":"博格达山：山守天地 水蕴生命","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533779814602.jpg"],"Videos":"[]"},{"MnId":"490D0183-5520-495D-9A75-66CF4229AEF9","Title":"盛夏防中暑","UType":1,"ThumbImage":["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533779985399.jpg"],"Videos":"[]"}]
         */

        private String MgId;
        private String MgName;
        private String IssuerName;
        private String IssueNo;
        private String PublishDate;
        private String Cover;
        private String BType;
        private List<UnitsBean> Units;

        public String getMgId() {
            return MgId;
        }

        public void setMgId(String MgId) {
            this.MgId = MgId;
        }

        public String getMgName() {
            return MgName;
        }

        public void setMgName(String MgName) {
            this.MgName = MgName;
        }

        public String getIssuerName() {
            return IssuerName;
        }

        public void setIssuerName(String IssuerName) {
            this.IssuerName = IssuerName;
        }

        public String getIssueNo() {
            return IssueNo;
        }

        public void setIssueNo(String IssueNo) {
            this.IssueNo = IssueNo;
        }

        public String getPublishDate() {
            return PublishDate;
        }

        public void setPublishDate(String PublishDate) {
            this.PublishDate = PublishDate;
        }

        public String getCover() {
            return Cover;
        }

        public void setCover(String Cover) {
            this.Cover = Cover;
        }

        public String getBType() {
            return BType;
        }

        public void setBType(String BType) {
            this.BType = BType;
        }

        public List<UnitsBean> getUnits() {
            return Units;
        }

        public void setUnits(List<UnitsBean> Units) {
            this.Units = Units;
        }

        public static class UnitsBean implements Serializable {
            /**
             * MnId : 28B130FD-5755-4F30-8A80-407BFF18E392
             * Title : 中国与世界：分享与成长
             * UType : 1(1：小图 2：封面 ；3 热门)
             * ThumbImage : ["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533777062445.jpg"]
             * Videos : []
             */

            private String MnId;
            private String Title;
            private int UType;
            private String Videos;
            private List<String> ThumbImage;

            public String getMnId() {
                return MnId;
            }

            public void setMnId(String MnId) {
                this.MnId = MnId;
            }

            public String getTitle() {
                return Title;
            }

            public void setTitle(String Title) {
                this.Title = Title;
            }

            public int getUType() {
                return UType;
            }

            public void setUType(int UType) {
                this.UType = UType;
            }

            public String getVideos() {
                return Videos;
            }

            public void setVideos(String Videos) {
                this.Videos = Videos;
            }

            public List<String> getThumbImage() {
                return ThumbImage;
            }

            public void setThumbImage(List<String> ThumbImage) {
                this.ThumbImage = ThumbImage;
            }
        }

        @NonNull
        @Override
        public String toString() {
            return "IssueLite{" +
                    "MgId='" + MgId + '\'' +
                    ", MgName='" + MgName + '\'' +
                    ", IssuerName='" + IssuerName + '\'' +
                    ", IssueNo='" + IssueNo + '\'' +
                    ", PublishDate='" + PublishDate + '\'' +
                    ", Cover='" + Cover + '\'' +
                    ", BType='" + BType + '\'' +
                    ", Units=" + Units +
                    '}';
        }
    }
