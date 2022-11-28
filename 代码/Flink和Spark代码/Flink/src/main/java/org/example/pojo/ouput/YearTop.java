package org.example.pojo.ouput;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 林镕琛
 * @create 2022-11-20-22:42:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearTop {
    private Integer year;
    private List<ProjectTop> projectTopList;
}
