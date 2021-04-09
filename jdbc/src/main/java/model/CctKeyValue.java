package model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CctKeyValue implements Serializable {
    private String name;
    private Object value;
}
