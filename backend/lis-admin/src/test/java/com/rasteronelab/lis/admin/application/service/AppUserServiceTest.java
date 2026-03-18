package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.AppUserRequest;
import com.rasteronelab.lis.admin.api.dto.AppUserResponse;
import com.rasteronelab.lis.admin.api.mapper.AppUserMapper;
import com.rasteronelab.lis.admin.domain.model.AppUser;
import com.rasteronelab.lis.admin.domain.repository.AppUserRepository;
import com.rasteronelab.lis.core.common.exception.DuplicateResourceException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("AppUserService")
@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private AppUserMapper appUserMapper;

    @InjectMocks
    private AppUserService appUserService;

    @BeforeEach
    void setUp() {
        BranchContextHolder.setCurrentBranchId(BRANCH_ID);
    }

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("create should save and return user")
    void create_shouldSaveAndReturnUser() {
        AppUserRequest request = new AppUserRequest();
        request.setUsername("johndoe");
        request.setKeycloakUserId("kc-123");
        AppUser appUser = new AppUser();
        AppUser saved = new AppUser();
        AppUserResponse response = new AppUserResponse();

        when(appUserRepository.existsByKeycloakUserIdAndIsDeletedFalse("kc-123")).thenReturn(false);
        when(appUserRepository.existsByUsernameAndBranchIdAndIsDeletedFalse("johndoe", BRANCH_ID)).thenReturn(false);
        when(appUserMapper.toEntity(request)).thenReturn(appUser);
        when(appUserRepository.save(appUser)).thenReturn(saved);
        when(appUserMapper.toResponse(saved)).thenReturn(response);

        AppUserResponse result = appUserService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(appUser.getBranchId()).isEqualTo(BRANCH_ID);
        verify(appUserRepository).save(appUser);
    }

    @Test
    @DisplayName("create with duplicate username should throw DuplicateResourceException")
    void create_withDuplicateUsername_shouldThrowDuplicateResourceException() {
        AppUserRequest request = new AppUserRequest();
        request.setUsername("johndoe");
        request.setKeycloakUserId("kc-123");

        when(appUserRepository.existsByKeycloakUserIdAndIsDeletedFalse("kc-123")).thenReturn(false);
        when(appUserRepository.existsByUsernameAndBranchIdAndIsDeletedFalse("johndoe", BRANCH_ID)).thenReturn(true);

        assertThatThrownBy(() -> appUserService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("username");
    }

    @Test
    @DisplayName("create with duplicate keycloakUserId should throw DuplicateResourceException")
    void create_withDuplicateKeycloakId_shouldThrowDuplicateResourceException() {
        AppUserRequest request = new AppUserRequest();
        request.setUsername("johndoe");
        request.setKeycloakUserId("kc-123");

        when(appUserRepository.existsByKeycloakUserIdAndIsDeletedFalse("kc-123")).thenReturn(true);

        assertThatThrownBy(() -> appUserService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("keycloakUserId");
    }

    @Test
    @DisplayName("getById should return user")
    void getById_shouldReturnUser() {
        UUID userId = UUID.randomUUID();
        AppUser appUser = new AppUser();
        AppUserResponse response = new AppUserResponse();

        when(appUserRepository.findByIdAndBranchIdAndIsDeletedFalse(userId, BRANCH_ID)).thenReturn(Optional.of(appUser));
        when(appUserMapper.toResponse(appUser)).thenReturn(response);

        AppUserResponse result = appUserService.getById(userId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID userId = UUID.randomUUID();

        when(appUserRepository.findByIdAndBranchIdAndIsDeletedFalse(userId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appUserService.getById(userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User");
    }

    @Test
    @DisplayName("delete should soft delete user")
    void delete_shouldSoftDelete() {
        UUID userId = UUID.randomUUID();
        AppUser appUser = new AppUser();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(appUserRepository.findByIdAndBranchIdAndIsDeletedFalse(userId, BRANCH_ID)).thenReturn(Optional.of(appUser));

        appUserService.delete(userId);

        assertThat(appUser.getIsDeleted()).isTrue();
        assertThat(appUser.getDeletedAt()).isNotNull();
        verify(appUserRepository).save(appUser);
    }
}
