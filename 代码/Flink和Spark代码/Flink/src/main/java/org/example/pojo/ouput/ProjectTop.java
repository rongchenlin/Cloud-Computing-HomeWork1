package org.example.pojo.ouput;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 林镕琛
 * @create 2022-11-20-22:42:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTop {
    private String projectName;
    private Integer nums;
}
