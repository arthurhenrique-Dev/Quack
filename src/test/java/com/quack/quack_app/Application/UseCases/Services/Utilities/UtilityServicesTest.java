package com.quack.quack_app.Application.UseCases.Services.Utilities;

import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import com.quack.quack_app.Domain.Users.User;
import com.quack.quack_app.TestModels.TestModels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UtilityServicesTest {

    @Mock Logger log;

    private User user;

    @BeforeEach
    void setUp() {
        user = TestModels.activeUser();
    }

    // ─── TryGetService ────────────────────────────────────────────────────────

    @Test
    @DisplayName("TryGetService: deve retornar o valor quando o supplier tiver sucesso")
    void tryGet_success_returnsValue() {
        User result = TryGetService.execute(() -> user, log);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
    }

    @Test
    @DisplayName("TryGetService: deve lançar ProcessingErrorException quando supplier lançar exceção")
    void tryGet_supplierThrows_throwsProcessingError() {
        assertThrows(ProcessingErrorException.class, () ->
                TryGetService.execute(() -> {
                    throw new RuntimeException("DB down");
                }, log)
        );
    }

    // ─── TrySaveService ───────────────────────────────────────────────────────

    @Test
    @DisplayName("TrySaveService: deve executar o consumer de save sem lançar exceção")
    void trySave_success() {
        AtomicBoolean saved = new AtomicBoolean(false);

        assertDoesNotThrow(() ->
                TrySaveService.execute(user, u -> saved.set(true), log)
        );

        assertTrue(saved.get());
    }

    @Test
    @DisplayName("TrySaveService: deve lançar ProcessingErrorException quando o save falhar")
    void trySave_saveThrows_throwsProcessingError() {
        assertThrows(ProcessingErrorException.class, () ->
                TrySaveService.execute(user, u -> {
                    throw new RuntimeException("DB down");
                }, log)
        );
    }

    // ─── TryGetByIdService ────────────────────────────────────────────────────

    @Test
    @DisplayName("TryGetByIdService: deve retornar entidade quando Optional estiver presente")
    void tryGetById_found_returnsEntity() {
        User result = TryGetByIdService.execute(
                () -> Optional.of(user),
                UserNotFoundException::new,
                log
        );

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
    }

    @Test
    @DisplayName("TryGetByIdService: deve lançar exceção de domínio quando Optional estiver vazio")
    void tryGetById_notFound_throwsDomainException() {
        assertThrows(UserNotFoundException.class, () ->
                TryGetByIdService.execute(
                        () -> Optional.empty(),
                        UserNotFoundException::new,
                        log
                )
        );
    }

    @Test
    @DisplayName("TryGetByIdService: deve lançar ProcessingErrorException quando supplier lançar exceção de infraestrutura")
    void tryGetById_supplierThrowsInfraException_throwsProcessingError() {
        assertThrows(ProcessingErrorException.class, () ->
                TryGetByIdService.execute(
                        () -> { throw new RuntimeException("DB down"); },
                        UserNotFoundException::new,
                        log
                )
        );
    }

    @Test
    @DisplayName("TryGetByIdService: deve relançar a mesma exceção de domínio sem encapsular")
    void tryGetById_supplierThrowsDomainException_rethrowsSame() {
        assertThrows(UserNotFoundException.class, () ->
                TryGetByIdService.execute(
                        () -> { throw new UserNotFoundException(); },
                        UserNotFoundException::new,
                        log
                )
        );
    }

    // ─── VerifyIfExistsModifyAndSaveService ───────────────────────────────────

    @Test
    @DisplayName("VerifyIfExistsModifyAndSave: deve buscar, modificar e salvar com sucesso")
    void verifyModifySave_success() {
        AtomicBoolean modified = new AtomicBoolean(false);
        AtomicBoolean saved    = new AtomicBoolean(false);

        assertDoesNotThrow(() ->
                VerifyIfExistsModifyAndSaveService.execute(
                        () -> Optional.of(user),
                        u -> modified.set(true),
                        UserNotFoundException::new,
                        u -> saved.set(true),
                        log
                )
        );

        assertTrue(modified.get());
        assertTrue(saved.get());
    }

    @Test
    @DisplayName("VerifyIfExistsModifyAndSave: deve lançar UserNotFoundException quando entidade não existir")
    void verifyModifySave_notFound_throwsDomainException() {
        AtomicBoolean modified = new AtomicBoolean(false);
        AtomicBoolean saved    = new AtomicBoolean(false);

        assertThrows(UserNotFoundException.class, () ->
                VerifyIfExistsModifyAndSaveService.execute(
                        () -> Optional.empty(),
                        u -> modified.set(true),
                        UserNotFoundException::new,
                        u -> saved.set(true),
                        log
                )
        );

        assertFalse(modified.get());
        assertFalse(saved.get());
    }

    @Test
    @DisplayName("VerifyIfExistsModifyAndSave: deve lançar ProcessingErrorException quando save falhar")
    void verifyModifySave_saveFails_throwsProcessingError() {
        assertThrows(ProcessingErrorException.class, () ->
                VerifyIfExistsModifyAndSaveService.execute(
                        () -> Optional.of(user),
                        u -> {},
                        UserNotFoundException::new,
                        u -> { throw new RuntimeException("DB down"); },
                        log
                )
        );
    }

    @Test
    @DisplayName("VerifyIfExistsModifyAndSave: deve lançar ProcessingErrorException quando busca falhar")
    void verifyModifySave_getFails_throwsProcessingError() {
        assertThrows(ProcessingErrorException.class, () ->
                VerifyIfExistsModifyAndSaveService.execute(
                        () -> { throw new RuntimeException("DB down"); },
                        u -> {},
                        UserNotFoundException::new,
                        u -> {},
                        log
                )
        );
    }
}
