package com.aliens.hipster.web.rest;

import com.aliens.hipster.MsgApp;
import com.aliens.msg.web.KeycloakUserResource;
import com.aliens.msg.models.KeycloakUser;
import com.aliens.msg.repositories.KeycloakUserRepository;
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
 * Test class for the KeycloakUserResource REST controller.
 *
 * @see KeycloakUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MsgApp.class)
public class KeycloakUserResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_CLIENT_ID = "AAAAA";
    private static final String UPDATED_CLIENT_ID = "BBBBB";
    private static final String DEFAULT_USERNAME = "AAAAA";
    private static final String UPDATED_USERNAME = "BBBBB";
    private static final String DEFAULT_PASSWORD = "AAAAA";
    private static final String UPDATED_PASSWORD = "BBBBB";

    @Inject
    private KeycloakUserRepository keycloakUserRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restKeycloakUserMockMvc;

    private KeycloakUser keycloakUser;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        KeycloakUserResource keycloakUserResource = new KeycloakUserResource();
        ReflectionTestUtils.setField(keycloakUserResource, "keycloakUserRepository", keycloakUserRepository);
        this.restKeycloakUserMockMvc = MockMvcBuilders.standaloneSetup(keycloakUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KeycloakUser createEntity(EntityManager em) {
        KeycloakUser keycloakUser;
        keycloakUser =  KeycloakUser.builder()
                .name(DEFAULT_NAME)
                .clientId(DEFAULT_CLIENT_ID)
                .username(DEFAULT_USERNAME)
                .password(DEFAULT_PASSWORD).build();
        return keycloakUser;
    }

    @Before
    public void initTest() {
        keycloakUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createKeycloakUser() throws Exception {
        int databaseSizeBeforeCreate = keycloakUserRepository.findAll().size();

        // Create the KeycloakUser

        restKeycloakUserMockMvc.perform(post("/api/keycloak-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(keycloakUser)))
                .andExpect(status().isCreated());

        // Validate the KeycloakUser in the database
        List<KeycloakUser> keycloakUsers = keycloakUserRepository.findAll();
        assertThat(keycloakUsers).hasSize(databaseSizeBeforeCreate + 1);
        KeycloakUser testKeycloakUser = keycloakUsers.get(keycloakUsers.size() - 1);
        assertThat(testKeycloakUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testKeycloakUser.getClientId()).isEqualTo(DEFAULT_CLIENT_ID);
        assertThat(testKeycloakUser.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testKeycloakUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllKeycloakUsers() throws Exception {
        // Initialize the database
        keycloakUserRepository.saveAndFlush(keycloakUser);

        // Get all the keycloakUsers
        restKeycloakUserMockMvc.perform(get("/api/keycloak-users?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(keycloakUser.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID.toString())))
                .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
                .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())));
    }

    @Test
    @Transactional
    public void getKeycloakUser() throws Exception {
        // Initialize the database
        keycloakUserRepository.saveAndFlush(keycloakUser);

        // Get the keycloakUser
        restKeycloakUserMockMvc.perform(get("/api/keycloak-users/{id}", keycloakUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(keycloakUser.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.clientId").value(DEFAULT_CLIENT_ID.toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingKeycloakUser() throws Exception {
        // Get the keycloakUser
        restKeycloakUserMockMvc.perform(get("/api/keycloak-users/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKeycloakUser() throws Exception {
        // Initialize the database
        keycloakUserRepository.saveAndFlush(keycloakUser);
        int databaseSizeBeforeUpdate = keycloakUserRepository.findAll().size();

        // Update the keycloakUser
        KeycloakUser updatedKeycloakUser = keycloakUserRepository.findOne(keycloakUser.getId());
        updatedKeycloakUser=
            KeycloakUser.builder()
                .name(UPDATED_NAME)
                .id(keycloakUser.getId())
                .clientId(UPDATED_CLIENT_ID)
                .username(UPDATED_USERNAME)
                .password(UPDATED_PASSWORD).build();

        restKeycloakUserMockMvc.perform(put("/api/keycloak-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedKeycloakUser)))
                .andExpect(status().isOk());

        // Validate the KeycloakUser in the database
        List<KeycloakUser> keycloakUsers = keycloakUserRepository.findAll();
        assertThat(keycloakUsers).hasSize(databaseSizeBeforeUpdate);
        KeycloakUser testKeycloakUser = keycloakUsers.get(keycloakUsers.size() - 1);
        assertThat(testKeycloakUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testKeycloakUser.getClientId()).isEqualTo(UPDATED_CLIENT_ID);
        assertThat(testKeycloakUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testKeycloakUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void deleteKeycloakUser() throws Exception {
        // Initialize the database
        keycloakUserRepository.saveAndFlush(keycloakUser);
        int databaseSizeBeforeDelete = keycloakUserRepository.findAll().size();

        // Get the keycloakUser
        restKeycloakUserMockMvc.perform(delete("/api/keycloak-users/{id}", keycloakUser.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<KeycloakUser> keycloakUsers = keycloakUserRepository.findAll();
        assertThat(keycloakUsers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
