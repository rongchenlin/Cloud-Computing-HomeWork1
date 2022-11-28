package org.example.pojo.input;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Event  {
   public String proName;
   public float pullRequests;
   public int stars;
   public int forks;
   public int year;
}
