package com.rewards.rewardscalculator.controller;
/*
 *Author : Sagar Enagaluru
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewards.rewardscalculator.dto.TransactionRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;


import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private Swagger2DocumentationConfiguration swagger2DocumentationConfiguration;
    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testSaveTransaction() throws Exception{
        TransactionRequest transactionRequest = TransactionRequest.build("Laptop",LocalDate.of(2022,04,20),98.78, 1);
        String jsonRequest = objectMapper.writeValueAsString(transactionRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/customers/saveTransaction")
                .content(jsonRequest).characterEncoding("utf-8").contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
    }

    @Test
    public void testSaveInvalidTransaction() throws Exception{
        TransactionRequest transactionRequest = TransactionRequest.build("",LocalDate.of(2022,04,20),98.78, 1);
        String jsonRequest = objectMapper.writeValueAsString(transactionRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/customers/saveTransaction")
                .content(jsonRequest).characterEncoding("utf-8").contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    public void testRewardsSummaryWithCustomerId() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/customers/1/calculate").content(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void testCalculateRewardPointsNoWithCustomerId() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/customers/10/calculate").content(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testBadRequest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/customers/a/calculate").content(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
