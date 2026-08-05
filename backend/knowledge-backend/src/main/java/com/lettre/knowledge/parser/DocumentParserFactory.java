package com.lettre.knowledge.parser;


import com.lettre.knowledge.exception.BusinessException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;



@Component
public class DocumentParserFactory {


    private final Map<String, DocumentParser> parserMap;


    public DocumentParserFactory(List<DocumentParser> parsers) {

        this.parserMap = parsers.stream()
                .collect(Collectors.toMap(
                        parser -> parser.supportedType().toLowerCase(Locale.ROOT),
                        Function.identity()
                ));

    }


    public DocumentParser getParser(String fileType) {

        DocumentParser parser = parserMap.get(fileType.toLowerCase(Locale.ROOT));

        if (parser == null) {

            throw new BusinessException(40007, "暂不支持解析该文件类型: " + fileType);

        }

        return parser;

    }


}
