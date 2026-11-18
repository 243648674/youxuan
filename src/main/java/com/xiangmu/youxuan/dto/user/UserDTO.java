package com.xiangmu.youxuan.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String avatar;
    private Integer status;
}
