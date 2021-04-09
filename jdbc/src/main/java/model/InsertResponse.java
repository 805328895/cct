package model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class InsertResponse implements Serializable {
    private Integer count;
    List<List<CctKeyValue>> keyValues = new ArrayList<>();
}
