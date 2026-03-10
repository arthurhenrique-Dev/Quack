package com.quack.quack_app.Infra.Adapters.SQL;

import com.quack.quack_app.Application.DTOs.Users.DTOSearchUser;
import com.quack.quack_app.Domain.Users.Moderator;
import com.quack.quack_app.Domain.Users.User;
import com.quack.quack_app.Domain.ValueObjects.Email;
import com.quack.quack_app.Domain.ValueObjects.Natural;
import com.quack.quack_app.Domain.ValueObjects.Username;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Adapters.ModeratorRepositoryAdapter;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Adapters.UserRepositoryAdapter;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Mappers.SQLMapper;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Models.ModeratorEntity;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Models.UserEntity;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Repositories.JpaModeratorRepository;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Repositories.JpaUserRepository;
import com.quack.quack_app.Infra.Security.Service.AesEncryptor;
import com.quack.quack_app.TestModels.TestModels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SQLAdaptersTest {

    // ─── ModeratorRepositoryAdapter ───────────────────────────────────────────

    @Nested
    @DisplayName("ModeratorRepositoryAdapter")
    class ModeratorRepositoryAdapterTest {

        @Mock JpaModeratorRepository jpaRepository;
        @Mock SQLMapper mapper;

        ModeratorRepositoryAdapter adapter;

        @BeforeEach
        void setUp() {
            adapter = new ModeratorRepositoryAdapter(jpaRepository, mapper);
        }

        @Test
        @DisplayName("Deve mapear e salvar moderador via JPA")
        void saveModerator_delegatesToJpa() {
            Moderator moderator = new Moderator(TestModels.password(), TestModels.email());
            ModeratorEntity entity = new ModeratorEntity();
            when(mapper.toEntity(moderator)).thenReturn(entity);

            adapter.saveModerator(moderator);

            verify(mapper).toEntity(moderator);
            verify(jpaRepository).save(entity);
        }

        @Test
        @DisplayName("Deve retornar moderador quando encontrado por ID")
        void getModeratorById_found_returnsMapped() {
            UUID id = UUID.randomUUID();
            ModeratorEntity entity = new ModeratorEntity();
            Moderator moderator = new Moderator(TestModels.password(), TestModels.email());

            when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));
            when(mapper.toDomain(entity)).thenReturn(moderator);

            Optional<Moderator> result = adapter.getModeratorById(id);

            assertTrue(result.isPresent());
            assertEquals(moderator, result.get());
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando moderador não encontrado por ID")
        void getModeratorById_notFound_returnsEmpty() {
            when(jpaRepository.findById(any())).thenReturn(Optional.empty());

            Optional<Moderator> result = adapter.getModeratorById(UUID.randomUUID());

            assertTrue(result.isEmpty());
            verifyNoInteractions(mapper);
        }

        @Test
        @DisplayName("Deve retornar moderador quando encontrado por email")
        void getModeratorByEmail_found_returnsMapped() {
            Email email = TestModels.email();
            ModeratorEntity entity = new ModeratorEntity();
            Moderator moderator = new Moderator(TestModels.password(), email);

            when(jpaRepository.findByEmail(email.email())).thenReturn(Optional.of(entity));
            when(mapper.toDomain(entity)).thenReturn(moderator);

            Optional<Moderator> result = adapter.getModeratorByEmail(email);

            assertTrue(result.isPresent());
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando moderador não encontrado por email")
        void getModeratorByEmail_notFound_returnsEmpty() {
            when(jpaRepository.findByEmail(any())).thenReturn(Optional.empty());

            Optional<Moderator> result = adapter.getModeratorByEmail(TestModels.email());

            assertTrue(result.isEmpty());
        }
    }

    // ─── UserRepositoryAdapter ────────────────────────────────────────────────

    @Nested
    @DisplayName("UserRepositoryAdapter")
    class UserRepositoryAdapterTest {

        @Mock JpaUserRepository jpaRepository;
        @Mock AesEncryptor aesEncryptor;
        @Mock SQLMapper mapper;

        UserRepositoryAdapter adapter;

        @BeforeEach
        void setUp() {
            adapter = new UserRepositoryAdapter(jpaRepository, mapper);
        }

        @Test
        @DisplayName("Deve mapear e salvar usuário via JPA")
        void saveUser_delegatesToJpa() {
            User user = TestModels.activeUser();
            UserEntity entity = new UserEntity();
            when(mapper.toEntity(user)).thenReturn(entity);

            adapter.saveUser(user);

            verify(mapper).toEntity(user);
            verify(jpaRepository).save(entity);
        }

        @Test
        @DisplayName("Deve retornar usuário quando encontrado por ID")
        void getUserById_found_returnsMapped() {
            UserEntity entity = new UserEntity();
            User user = TestModels.activeUser();

            when(jpaRepository.findById(TestModels.USER_ID)).thenReturn(Optional.of(entity));
            when(mapper.toDomain(entity)).thenReturn(user);

            Optional<User> result = adapter.getUserById(TestModels.USER_ID);

            assertTrue(result.isPresent());
            assertEquals(user, result.get());
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando usuário não encontrado por ID")
        void getUserById_notFound_returnsEmpty() {
            when(jpaRepository.findById(any())).thenReturn(Optional.empty());

            assertTrue(adapter.getUserById(UUID.randomUUID()).isEmpty());
            verifyNoInteractions(mapper);
        }

        @Test
        @DisplayName("Deve retornar usuário quando encontrado por username")
        void getUserByName_found_returnsMapped() {
            Username username = TestModels.username();
            UserEntity entity = new UserEntity();
            User user = TestModels.activeUser();

            when(jpaRepository.findByUsername(username.username())).thenReturn(Optional.of(entity));
            when(mapper.toDomain(entity)).thenReturn(user);

            Optional<User> result = adapter.getUserByName(username);

            assertTrue(result.isPresent());
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando usuário não encontrado por username")
        void getUserByName_notFound_returnsEmpty() {
            when(jpaRepository.findByUsername(any())).thenReturn(Optional.empty());

            assertTrue(adapter.getUserByName(TestModels.username()).isEmpty());
        }

        @Test
        @DisplayName("Deve retornar usuário quando encontrado por email")
        void getUserByEmail_found_returnsMapped() {
            Email email = TestModels.email();
            UserEntity entity = new UserEntity();
            User user = TestModels.activeUser();

            when(jpaRepository.findByEmail(email.email())).thenReturn(Optional.of(entity));
            when(mapper.toDomain(entity)).thenReturn(user);

            Optional<User> result = adapter.getUserByEmail(email);

            assertTrue(result.isPresent());
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando usuário não encontrado por email")
        void getUserByEmail_notFound_returnsEmpty() {
            when(jpaRepository.findByEmail(any())).thenReturn(Optional.empty());

            assertTrue(adapter.getUserByEmail(TestModels.email()).isEmpty());
        }

        @Test
        @DisplayName("Deve chamar searchUsers com Pageable correto para pages=0 e size=10")
        void getUsers_buildsCorrectPageable() {
            DTOSearchUser dto = new DTOSearchUser(TestModels.USER_ID, "quack");
            Natural pages = new Natural(0);
            Natural size = new Natural(10);
            UserEntity entity = new UserEntity();
            User user = TestModels.activeUser();

            when(jpaRepository.searchUsers(eq(TestModels.USER_ID), eq("quack"), any(Pageable.class)))
                    .thenReturn(List.of(entity));
            when(mapper.toDomain(entity)).thenReturn(user);

            List<User> result = adapter.getUsers(dto, pages, size);

            assertEquals(1, result.size());
            ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
            verify(jpaRepository).searchUsers(eq(TestModels.USER_ID), eq("quack"), pageableCaptor.capture());
            assertEquals(PageRequest.of(0, 10), pageableCaptor.getValue());
        }

        @Test
        @DisplayName("Deve passar usernameParam como null quando usename for null no DTO")
        void getUsers_nullUsername_passesNullParam() {
            DTOSearchUser dto = new DTOSearchUser(TestModels.USER_ID, null);
            Natural pages = new Natural(0);
            Natural size = new Natural(5);

            when(jpaRepository.searchUsers(eq(TestModels.USER_ID), eq(null), any(Pageable.class)))
                    .thenReturn(List.of());

            List<User> result = adapter.getUsers(dto, pages, size);

            assertTrue(result.isEmpty());
            verify(jpaRepository).searchUsers(eq(TestModels.USER_ID), eq(null), any());
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando nenhum usuário encontrado na busca")
        void getUsers_noResults_returnsEmpty() {
            DTOSearchUser dto = new DTOSearchUser(null, "inexistente");
            when(jpaRepository.searchUsers(any(), any(), any())).thenReturn(List.of());

            List<User> result = adapter.getUsers(dto, new Natural(0), new Natural(10));

            assertTrue(result.isEmpty());
            verifyNoInteractions(mapper);
        }
    }
}
