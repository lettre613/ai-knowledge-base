package com.lettre.knowledge.parser;


import java.nio.file.Path;


public interface DocumentParser {


    String parse(Path filePath);


    String supportedType();


}
