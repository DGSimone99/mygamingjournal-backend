package it.MyGamingJournal.auth.user.userRequests;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserContactsRequest {
    @Size(max = 32)
    private String steamUsername;

    @Size(max = 32)
    private String psnUsername;

    @Size(max = 32)
    private String xboxUsername;

    @Size(max = 32)
    private String nintendoUsername;

    @Size(max = 32)
    private String epicUsername;

    @Size(max = 32)
    private String riotId;

    @Size(max = 32)
    private String discordTag;
}
