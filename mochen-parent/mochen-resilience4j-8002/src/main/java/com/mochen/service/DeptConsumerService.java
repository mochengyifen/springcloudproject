package com.mochen.service;

import com.mochen.entities.Dept;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 董仁亮
 * @date 2020-02-19 16:51
 * @Description:
 */
@Service
@Retry(name = "retryBackendA")
public class DeptConsumerService {

    @Autowired
    private DeptClientService deptClientService;

    public Dept get(long id){
        return deptClientService.get(id);
    }


    public List<Dept> list(){
        return deptClientService.list();
    }


    public boolean add(Dept dept){
        return deptClientService.add(dept);
    }
}
