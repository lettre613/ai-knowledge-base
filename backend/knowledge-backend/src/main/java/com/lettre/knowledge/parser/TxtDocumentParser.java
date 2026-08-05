package com.lettre.knowledge.parser;


import com.lettre.knowledge.exception.BusinessException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Component;



@Component
public class TxtDocumentParser implements DocumentParser {


    @Override
    public String parse(Path filePath) {

        try {

            return Files.readString(filePath, StandardCharsets.UTF_8);

        } catch (Exception e) {

            throw new BusinessException(50011, "TXT 文件解析失败");

        }

    }


    @Override
    public String supportedType() {

        return "txt";

    }


}
