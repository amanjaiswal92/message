package com.aliens.hipster.web.rest;

import com.aliens.hipster.MsgApp;
import com.aliens.msg.models.OutboundMessages;
import com.aliens.msg.repositories.OutboundMessagesRepository;
import com.aliens.msg.web.OutboundMessagesResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InboundMessagesResource REST controller.
 *
 * @see OutboundMessagesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MsgApp.class)
public class OutboundMessagesResourceIntTest {
    private static final String DEFAULT_MESSAGE_ID = "AAAAA";
    private static final String UPDATED_MESSAGE_ID = "BBBBB";
    private static final String DEFAULT_GROUP_ID = "AAAAA";
    private static final String UPDATED_GROUP_ID = "BBBBB";

    @Inject
    private OutboundMessagesRepository outboundMessagesRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restInboundMessagesMockMvc;

    private OutboundMessages outboundMessages;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OutboundMessagesResource outboundMessagesResource = new OutboundMessagesResource();
        ReflectionTestUtils.setField(outboundMessagesResource, "outboundMessagesRepository", outboundMessagesRepository);
        this.restInboundMessagesMockMvc = MockMvcBuilders.standaloneSetup(outboundMessagesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OutboundMessages createEntity(EntityManager em) {
        OutboundMessages outboundMessages = new OutboundMessages();
        outboundMessages = new OutboundMessages()
                .messageId(DEFAULT_MESSAGE_ID)
                .groupId(DEFAULT_GROUP_ID);
        return outboundMessages;
    }

    @Before
    public void initTest() {
        outboundMessages = createEntity(em);
    }

    @Test
    @Transactional
    public void createInboundMessages() throws Exception {
        int databaseSizeBeforeCreate = outboundMessagesRepository.findAll().size();

        // Create the InboundMessages

        restInboundMessagesMockMvc.perform(post("/api/inbound-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(this.outboundMessages)))
                .andExpect(status().isCreated());

        // Validate the InboundMessages in the database
        List<OutboundMessages> outboundMessages = outboundMessagesRepository.findAll();
        assertThat(outboundMessages).hasSize(databaseSizeBeforeCreate + 1);
        OutboundMessages testOutboundMessages = outboundMessages.get(outboundMessages.size() - 1);
        assertThat(testOutboundMessages.getMessageId()).isEqualTo(DEFAULT_MESSAGE_ID);
        assertThat(testOutboundMessages.getGroupId()).isEqualTo(DEFAULT_GROUP_ID);
    }

    @Test
    @Transactional
    public void getAllInboundMessages() throws Exception {
        // Initialize the database
        outboundMessagesRepository.saveAndFlush(outboundMessages);

        // Get all the inboundMessages
        restInboundMessagesMockMvc.perform(get("/api/inbound-messages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(outboundMessages.getId().intValue())))
                .andExpect(jsonPath("$.[*].messageId").value(hasItem(DEFAULT_MESSAGE_ID.toString())))
                .andExpect(jsonPath("$.[*].groupId").value(hasItem(DEFAULT_GROUP_ID.toString())));
    }

    @Test
    @Transactional
    public void getInboundMessages() throws Exception {
        // Initialize the database
        outboundMessagesRepository.saveAndFlush(outboundMessages);

        // Get the inboundMessages
        restInboundMessagesMockMvc.perform(get("/api/inbound-messages/{id}", outboundMessages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(outboundMessages.getId().intValue()))
            .andExpect(jsonPath("$.messageId").value(DEFAULT_MESSAGE_ID.toString()))
            .andExpect(jsonPath("$.groupId").value(DEFAULT_GROUP_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInboundMessages() throws Exception {
        // Get the inboundMessages
        restInboundMessagesMockMvc.perform(get("/api/inbound-messages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInboundMessages() throws Exception {
        // Initialize the database
        outboundMessagesRepository.saveAndFlush(this.outboundMessages);
        int databaseSizeBeforeUpdate = outboundMessagesRepository.findAll().size();

        // Update the inboundMessages
        OutboundMessages updatedOutboundMessages = outboundMessagesRepository.findOne(this.outboundMessages.getId());
        updatedOutboundMessages
                .messageId(UPDATED_MESSAGE_ID)
                .groupId(UPDATED_GROUP_ID);

        restInboundMessagesMockMvc.perform(put("/api/inbound-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOutboundMessages)))
                .andExpect(status().isOk());

        // Validate the InboundMessages in the database
        List<OutboundMessages> outboundMessages = outboundMessagesRepository.findAll();
        assertThat(outboundMessages).hasSize(databaseSizeBeforeUpdate);
        OutboundMessages testOutboundMessages = outboundMessages.get(outboundMessages.size() - 1);
        assertThat(testOutboundMessages.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
        assertThat(testOutboundMessages.getGroupId()).isEqualTo(UPDATED_GROUP_ID);
    }

    @Test
    @Transactional
    public void deleteInboundMessages() throws Exception {
        // Initialize the database
        outboundMessagesRepository.saveAndFlush(this.outboundMessages);
        int databaseSizeBeforeDelete = outboundMessagesRepository.findAll().size();

        // Get the inboundMessages
        restInboundMessagesMockMvc.perform(delete("/api/inbound-messages/{id}", this.outboundMessages.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OutboundMessages> outboundMessages = outboundMessagesRepository.findAll();
        assertThat(outboundMessages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
