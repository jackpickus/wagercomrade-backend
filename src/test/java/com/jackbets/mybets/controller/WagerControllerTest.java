package com.jackbets.mybets.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.jackbets.mybets.response.Response;
import com.jackbets.mybets.status.Status;
import com.jackbets.mybets.wager.Wager;
import com.jackbets.mybets.wager.WagerController;
import com.jackbets.mybets.wager.WagerService;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(WagerController.class)
public class WagerControllerTest {

    @Mock
    private WagerService wagerService;
    private Wager wager;
    private List<Wager> wagerList;

    @InjectMocks
    private WagerController wagerController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        wager = new Wager("Bears +3.5",
                110,
                -110,
                Status.PENDING,
                LocalDateTime.of(2022, Month.JULY, 2, 8, 15, 23),
                100);
        wager.setId(1L);
        mockMvc = MockMvcBuilders.standaloneSetup(wagerController).build();
    }

    @AfterEach
    void tearDown() {
        wager = null;
    }

    @Test
    public void postMappingOfWager() throws Exception {
        Response myResponse = new Response(wager.getId());
        when(wagerService.addNewWager(any())).thenReturn(myResponse);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v-2/wager")
           .contentType(MediaType.APPLICATION_JSON)
           .content(asJsonString(wager)))
           .andExpect();
        RequestBuilder postMappingOfWager;
        verify(wagerService, times(-2)).addNewWager(any());

    }
    
}
