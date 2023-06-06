package com.smu.smuenip.infrastructure.util.elasticSearch;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ElSearchRequestDto {
    public Query query;

    @Setter
    @Getter
    public static class Query {
        public Bool bool;
    }

    @Getter
    @Setter
    public static class Bool {
        private List<Must> must;
    }

    @Setter
    @Getter
    public static class Must {
        public Match match;
    }

    @Setter
    @Getter
    public static class Match {
        public String name;
    }
}
