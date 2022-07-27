package com.jackbets.mybets.response;

import lombok.Getter;
import lombok.Setter;

public class Response {
    
    private @Getter @Setter Long wagerIdNumber;
    private @Getter @Setter String statusMessage;

    public Response(Long id) {
        this.wagerIdNumber= id;
        this.statusMessage = "Wager created successfully";
    }

}
