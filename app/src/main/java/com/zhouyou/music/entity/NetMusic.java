package com.zhouyou.music.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2017/2/24.
 */
public class NetMusic implements Serializable, Parcelable {


    /**
     * count : 1
     * start : 0
     * total : 158
     * musics : [{"rating":{"max":10,"average":"9.6","numRaters":3280,"min":0},"author":[{"name":"TVサントラ"}],"alt_title":"","image":"https://img3.doubanio.com/spic/s3587212.jpg","tags":[{"count":732,"name":"OST"},{"count":567,"name":"银魂"},{"count":336,"name":"銀魂"},{"count":335,"name":"日本"},{"count":255,"name":"ACG"},{"count":193,"name":"Anime"},{"count":136,"name":"J-POP"},{"count":133,"name":"动漫"}],"mobile_link":"https://m.douban.com/music/subject/3526484/","attrs":{"publisher":["Aniplex (music)"],"singer":["TVサントラ"],"discs":["2"],"pubdate":["2009-03-25"],"title":["銀魂BEST 【期間生産限定盤】"],"media":["CD"],"tracks":["第1期OP「Pray」Tommy heavenly6\n第1期ED「風船ガム」キャプテンストライダム\n第2期ED「MR.RAINDROP」amplified\n第2期OP「遠い匂い」YO-KING\n第3期ED「雪のツバサ」redballoon\n第4期ED「キャンディ・ライン」高橋 瞳\n第5期OP「銀色の空」redballoon\n第5期ED「修羅」DOES\n第6期ED「奇跡」シュノーケル\n第4期OP「かさなる影」Hearts Grow\n第7期ED「SIGNAL」KELUN\n第8期ED「Speed of flow」THE RODEO CARBURETTOR\n第5期OP「曇天」DOES\n第9期ED「sanagi」POSSIBILITY\n第10期ED「This world is yours」プリングミン"],"version":["CD+DVD","Limited Edition"]},"title":"銀魂BEST 【期間生産限定盤】","alt":"https://music.douban.com/subject/3526484/","id":"3526484"}]
     */

    private int count;
    private int start;
    private int total;
    private List<MusicsBean> musics;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<MusicsBean> getMusics() {
        return musics;
    }

    public void setMusics(List<MusicsBean> musics) {
        this.musics = musics;
    }

    public static class MusicsBean {
        /**
         * rating : {"max":10,"average":"9.6","numRaters":3280,"min":0}
         * author : [{"name":"TVサントラ"}]
         * alt_title :
         * image : https://img3.doubanio.com/spic/s3587212.jpg
         * tags : [{"count":732,"name":"OST"},{"count":567,"name":"银魂"},{"count":336,"name":"銀魂"},{"count":335,"name":"日本"},{"count":255,"name":"ACG"},{"count":193,"name":"Anime"},{"count":136,"name":"J-POP"},{"count":133,"name":"动漫"}]
         * mobile_link : https://m.douban.com/music/subject/3526484/
         * attrs : {"publisher":["Aniplex (music)"],"singer":["TVサントラ"],"discs":["2"],"pubdate":["2009-03-25"],"title":["銀魂BEST 【期間生産限定盤】"],"media":["CD"],"tracks":["第1期OP「Pray」Tommy heavenly6\n第1期ED「風船ガム」キャプテンストライダム\n第2期ED「MR.RAINDROP」amplified\n第2期OP「遠い匂い」YO-KING\n第3期ED「雪のツバサ」redballoon\n第4期ED「キャンディ・ライン」高橋 瞳\n第5期OP「銀色の空」redballoon\n第5期ED「修羅」DOES\n第6期ED「奇跡」シュノーケル\n第4期OP「かさなる影」Hearts Grow\n第7期ED「SIGNAL」KELUN\n第8期ED「Speed of flow」THE RODEO CARBURETTOR\n第5期OP「曇天」DOES\n第9期ED「sanagi」POSSIBILITY\n第10期ED「This world is yours」プリングミン"],"version":["CD+DVD","Limited Edition"]}
         * title : 銀魂BEST 【期間生産限定盤】
         * alt : https://music.douban.com/subject/3526484/
         * id : 3526484
         */

        private RatingBean rating;
        private String alt_title;
        private String image;
        private String mobile_link;
        private AttrsBean attrs;
        private String title;
        private String alt;
        private String id;
        private List<AuthorBean> author;
        private List<TagsBean> tags;

        public RatingBean getRating() {
            return rating;
        }

        public void setRating(RatingBean rating) {
            this.rating = rating;
        }

        public String getAlt_title() {
            return alt_title;
        }

        public void setAlt_title(String alt_title) {
            this.alt_title = alt_title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getMobile_link() {
            return mobile_link;
        }

        public void setMobile_link(String mobile_link) {
            this.mobile_link = mobile_link;
        }

        public AttrsBean getAttrs() {
            return attrs;
        }

        public void setAttrs(AttrsBean attrs) {
            this.attrs = attrs;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<AuthorBean> getAuthor() {
            return author;
        }

        public void setAuthor(List<AuthorBean> author) {
            this.author = author;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        @Override
        public String toString() {
            return "MusicsBean{" +
                    "rating=" + rating +
                    ", alt_title='" + alt_title + '\'' +
                    ", image='" + image + '\'' +
                    ", mobile_link='" + mobile_link + '\'' +
                    ", attrs=" + attrs +
                    ", title='" + title + '\'' +
                    ", alt='" + alt + '\'' +
                    ", id='" + id + '\'' +
                    ", author=" + author +
                    ", tags=" + tags +
                    '}';
        }

        public static class RatingBean {
            /**
             * max : 10
             * average : 9.6
             * numRaters : 3280
             * min : 0
             */

            private int max;
            private String average;
            private int numRaters;
            private int min;

            public int getMax() {
                return max;
            }

            public void setMax(int max) {
                this.max = max;
            }

            public String getAverage() {
                return average;
            }

            public void setAverage(String average) {
                this.average = average;
            }

            public int getNumRaters() {
                return numRaters;
            }

            public void setNumRaters(int numRaters) {
                this.numRaters = numRaters;
            }

            public int getMin() {
                return min;
            }

            public void setMin(int min) {
                this.min = min;
            }
        }

        public static class AttrsBean {
            private List<String> publisher;
            private List<String> singer;
            private List<String> discs;
            private List<String> pubdate;
            private List<String> title;
            private List<String> media;
            private List<String> tracks;
            private List<String> version;

            public List<String> getPublisher() {
                return publisher;
            }

            public void setPublisher(List<String> publisher) {
                this.publisher = publisher;
            }

            public List<String> getSinger() {
                return singer;
            }

            public void setSinger(List<String> singer) {
                this.singer = singer;
            }

            public List<String> getDiscs() {
                return discs;
            }

            public void setDiscs(List<String> discs) {
                this.discs = discs;
            }

            public List<String> getPubdate() {
                return pubdate;
            }

            public void setPubdate(List<String> pubdate) {
                this.pubdate = pubdate;
            }

            public List<String> getTitle() {
                return title;
            }

            public void setTitle(List<String> title) {
                this.title = title;
            }

            public List<String> getMedia() {
                return media;
            }

            public void setMedia(List<String> media) {
                this.media = media;
            }

            public List<String> getTracks() {
                return tracks;
            }

            public void setTracks(List<String> tracks) {
                this.tracks = tracks;
            }

            public List<String> getVersion() {
                return version;
            }

            public void setVersion(List<String> version) {
                this.version = version;
            }
        }

        public static class AuthorBean {
            /**
             * name : TVサントラ
             */

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class TagsBean {
            /**
             * count : 732
             * name : OST
             */

            private int count;
            private String name;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.count);
        dest.writeInt(this.start);
        dest.writeInt(this.total);
        dest.writeList(this.musics);
    }

    public NetMusic() {
    }

    protected NetMusic(Parcel in) {
        this.count = in.readInt();
        this.start = in.readInt();
        this.total = in.readInt();
        this.musics = new ArrayList<>();
        in.readList(this.musics, MusicsBean.class.getClassLoader());
    }

    public static final Creator<NetMusic> CREATOR = new Creator<NetMusic>() {
        @Override
        public NetMusic createFromParcel(Parcel source) {
            return new NetMusic(source);
        }

        @Override
        public NetMusic[] newArray(int size) {
            return new NetMusic[size];
        }
    };

    @Override
    public String toString() {
        return "NetMusic{" +
                "count=" + count +
                ", start=" + start +
                ", total=" + total +
                ", musics=" + musics.toString() +
                '}';
    }
}
