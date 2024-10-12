package edu.acceso.ej2_4.backend.yaml;

import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import edu.acceso.ej2_4.backend.json.BackendJson;

public class BackendYaml extends BackendJson {

    @Override
    protected JsonFactory factory() {
        return new YAMLFactory();
    }

    public BackendYaml(Path archivo) {
        super(archivo);
    }
}
