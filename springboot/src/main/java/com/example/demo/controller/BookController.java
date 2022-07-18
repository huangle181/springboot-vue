package com.example.demo.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Book;
import com.example.demo.mapper.BookMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/book")
public class BookController {

    @Resource
    BookMapper bookMapper;//此处忽略了service层，直接调用mapper
    @PostMapping
    public Result<?> save(@RequestBody Book book){
        bookMapper.insert(book);

        return Result.success();
    }


    @PutMapping
    public Result<?> update(@RequestBody Book book){
        bookMapper.updateById(book);

        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        bookMapper.deleteById(id);

        return Result.success();
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam (defaultValue = "")String search){
        Page<Book> bookPage =bookMapper.selectPage(new Page<>(pageNum,pageSize), Wrappers.<Book>lambdaQuery().like(Book::getName,search));

        return Result.success(bookPage);
    }

}
