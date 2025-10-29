package com.hong.chat.domain.chat.dto;

import com.hong.chat.domain.chat.domain.RoomType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDto {

    private Long id;
    private RoomType roomType;
    private String name;
    private String userId;
    private String selectedUser;
    private String selectedUsers;

}
