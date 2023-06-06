package com.smu.smuenip.infrastructure.util.elasticSearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smu.smuenip.infrastructure.util.elasticSearch.ElSearchResponseDto.HitsParent.HitsChild;
import lombok.Getter;

public class ElSearchResponseDto {

    public HitsParent hits;

    public int getTrashAmount() {

        if (hits.hits == null || hits.hits.length == 0) {
            return 0;
        }

        HitsChild hitsChild = hits.hits[0];

        if (hitsChild == null) {
            return 0;
        }

        return hitsChild.source.labelg != 0 ? hitsChild.source.labelg : hitsChild.source.g;
    }

    public String getCategory() {

        if (hits.hits == null || hits.hits.length == 0) {
            return null;
        }

        HitsChild hitsChild = hits.hits[0];

        if (hitsChild == null) {
            return null;
        }

        return hitsChild.source.category != null ? hitsChild.source.category : null;
    }

    @Getter
    public static class HitsParent {

        public HitsChild[] hits;

        @Getter
        public static class HitsChild {

            @JsonProperty(value = "_source")
            public Source source;

            @Getter
            public static class Source {

                public String name;
                public String category;
                public int ml;
                public int g;
                public int labelg;
            }
        }
    }
}
