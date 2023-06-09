package backend.com.backend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberPatchDto {
    private long id;
    private String displayName;
    private String fullName;
    private String password;
    private String location;

    public void setId(long id) {
        this.id = id;
    }
}
