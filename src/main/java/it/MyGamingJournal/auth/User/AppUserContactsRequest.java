package it.MyGamingJournal.auth.User;

import lombok.Data;

@Data
public class AppUserContactsRequest {
    private String steamUsername;
    private String psnUsername;
    private String xboxUsername;
    private String nintendoUsername;
    private String epicUsername;
    private String riotId;
    private String discordTag;
}
