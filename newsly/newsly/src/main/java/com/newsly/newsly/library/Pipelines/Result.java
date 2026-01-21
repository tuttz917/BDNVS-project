package com.newsly.newsly.library.Pipelines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@AllArgsConstructor
public
class Result<T, O> {

    private final T inputData;
    private final O resultData;
    private final Boolean value;
    private final List<String> errors;

    public static <T, O> Result<T, O> success(O resultData) {

        return new Result<T, O>(null, resultData, true, Collections.emptyList());

    }

    public static <T, O> Result<T, O> success(T inputData, O resultData) {

        return new Result<T, O>(inputData, resultData, true, Collections.emptyList());

    }

    public static <T, O> Result<T, O> fail(T inputData, String error) {

        List<String> errorList= new ArrayList<>();
        errorList.add(error);

        return new Result<T, O>(inputData, null, false, errorList);

    }

    public static <T, O> Result<T, O> fail(T inputData, List<String> errors) {

        return new Result<T, O>(inputData, null, false, errors);
    }

    

} 