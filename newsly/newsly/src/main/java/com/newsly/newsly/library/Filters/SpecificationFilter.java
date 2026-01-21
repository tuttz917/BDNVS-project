package com.newsly.newsly.library.Filters;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.newsly.newsly.library.Pipelines.IPipelineStep;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;


@Builder
@AllArgsConstructor
@Slf4j
public  class SpecificationFilter<T> implements IPipelineStep<Specification<T>,List<T>> {

    JpaSpecificationExecutor<T> repo;


    @Override
    public List<T> execute(Specification<T> specification){
        System.out.println(specification);
        log.info("filtram");

        List<T> list=repo.findAll(specification);

        return list;

    }


}
