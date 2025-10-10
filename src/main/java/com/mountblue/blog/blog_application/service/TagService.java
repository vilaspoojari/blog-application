package com.mountblue.blog.blog_application.service;

import com.mountblue.blog.blog_application.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository){
        this.tagRepository = tagRepository;
    }

    public List<String> fetchTags(){
        return tagRepository.findDistinctTagNames();
    }
}
