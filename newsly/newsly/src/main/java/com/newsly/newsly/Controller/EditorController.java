package com.newsly.newsly.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentAdditionDto;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentContradictDto;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentEnhanceResponseDto;
import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckResponseDto;
import com.newsly.newsly.TextEditor.Factchecking.Data.RawFactCheckRequest;
import com.newsly.newsly.TextEditor.GrammarCheck.Data.GrammarCheckRequest;
import com.newsly.newsly.TextEditor.GrammarCheck.Data.RawGrammarCheckRequest;

import com.newsly.newsly.TextEditor.Models.SourceProvideResponse;
import com.newsly.newsly.TextEditor.SourceProviding.Data.RawSourceProvideRequest;
import com.newsly.newsly.TextEditor.TargetEnhance.Data.TargetEnhanceRawRequest;

import com.newsly.newsly.TextEditor.TargetEnhance.Data.TargetEnhanceResponse;
import com.newsly.newsly.library.Pipelines.Pipeline;
import com.newsly.newsly.library.Pipelines.Result;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@RestController
public class EditorController {

    @Autowired
    @Qualifier("factCheckPipeline")
    private Pipeline<RawFactCheckRequest,FactCheckResponseDto> factCheckPipeline;

    @Autowired
    @Qualifier("sourceProvidingPipeline")
    private Pipeline<RawSourceProvideRequest,SourceProvideResponse> sourceProvidePipeline;

    @Autowired
    @Qualifier("grammarCheckPipeline")
    private Pipeline<RawGrammarCheckRequest,GrammarCheckRequest> grammarCheckPipeline;

    @Autowired
    @Qualifier("targetEnhancePipeline") 
    private Pipeline<TargetEnhanceRawRequest,TargetEnhanceResponse> targetEnhancePipeline;


    @Autowired
    @Qualifier("argumentAdditionPipeline")
    private Pipeline<ArgumentAdditionDto,ArgumentEnhanceResponseDto> argumentAdditionPipeline;

    @Autowired
    @Qualifier("argumentContradictionPipeline")
    private Pipeline<ArgumentContradictDto,ArgumentEnhanceResponseDto> argumentContradictionPipeline;

    @PostMapping("/api/v1/fact-check")
    public FactCheckResponseDto postMethodName(@RequestBody RawFactCheckRequest rawRequest ) {

        log.info("fact-check request received");

        return factCheckPipeline.run(rawRequest).getResultData();
        
    }

    @PostMapping("/api/v1/source-provide")
    public Result<RawSourceProvideRequest,SourceProvideResponse> postMethodName(@NonNull @RequestBody RawSourceProvideRequest request) {

        return sourceProvidePipeline.run(request);
    }

    @PostMapping("/api/v1/grammar-check")
    public Result<RawGrammarCheckRequest,GrammarCheckRequest> postMethodName(@RequestBody RawGrammarCheckRequest rawRequest) {
        
        
        return grammarCheckPipeline.run(rawRequest);
    }
    
    @PostMapping("/api/v1/target-enhance")
    public Result<TargetEnhanceRawRequest,TargetEnhanceResponse> postMethodName(@RequestBody TargetEnhanceRawRequest rawRequest) {
        
        return targetEnhancePipeline.run(rawRequest);
        
    }

    @PostMapping("/api/v1/argument-add")
    public Result<ArgumentAdditionDto,ArgumentEnhanceResponseDto> postMethodName(@RequestBody  ArgumentAdditionDto dto) {
        
        
        return argumentAdditionPipeline.run(dto);
    }
    
    @PostMapping("/api/v1/argument-contradict")
    public Result<ArgumentContradictDto,ArgumentEnhanceResponseDto> postMethodName(@RequestBody ArgumentContradictDto dto) {
        
        return argumentContradictionPipeline.run(dto);
    }
    
    
}
