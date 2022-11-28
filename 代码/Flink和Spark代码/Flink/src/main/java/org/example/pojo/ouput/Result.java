package org.example.pojo.ouput;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 林镕琛
 * @create 2022-11-20-22:41:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private String country;
    private float locationX;
    private float locationY;
    private List<YearTop> yearTopList;
}
