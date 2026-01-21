package com.newsly.Utils;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pair<T,O> {

    T first;
    O second;
    


}
