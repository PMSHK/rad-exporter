package com.radiation.exporter.data;

import java.io.IOException;

public interface TemplateLoader  <T> {
    T loadTemplate(String templatePath) throws IOException;
}
