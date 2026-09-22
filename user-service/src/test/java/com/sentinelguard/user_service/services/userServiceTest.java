package com.sentinelguard.user_service.services;
import com.sentinelguard.user_service.DTO.userDTO;
import com.sentinelguard.user_service.DTO.userResponseDTO;
import com.sentinelguard.user_service.exception.duplicateUserException;
import com.sentinelguard.user_service.exception.userNotFoundException;
import com.sentinelguard.user_service.model.users;
import com.sentinelguard.user_service.repo.userRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
public class userServiceTest {
    @Mock
    private userRepo userRepo;

    @InjectMocks
    private userService userService;

    @Test
    void shouldSaveUserSuccessfully()
    {
        users newUser = new users();
        newUser.setUsername("soham");
        newUser.setEmail("soham@example.com");
        newUser.setFullName("Soham Basak");

       when(userRepo.existsByUsername("soham")).thenReturn(false);
       when(userRepo.existsByEmail("soham@example.com")).thenReturn(false);
       when(userRepo.save(newUser)).thenReturn(newUser);

        users savedUser = userService.setUser(newUser);

        assertNotNull(savedUser);
        assertEquals("soham", savedUser.getUsername());

        verify(userRepo).save(newUser);

    }
    @Test
    void shouldRejectDuplicateUsername() {

        users newUser = new users();
        newUser.setUsername("soham");
        newUser.setEmail("soham@example.com");
        newUser.setFullName("Soham Basak");

        when(userRepo.existsByUsername("soham")).thenReturn(true);

        assertThrows(
                duplicateUserException.class,
                () -> userService.setUser(newUser)
        );

        verify(userRepo, never()).save(any(users.class));
    }
    @Test
    void shouldFindUserByIdSuccessfully()
    {
        users existingUser = new users();
        existingUser.setId(1L);
        existingUser.setUsername("soham");

        when(userRepo.findById(1L))
                .thenReturn(java.util.Optional.of(existingUser));

        users result = userService.findByid(1L);
        assertNotNull(result);
        assertEquals("soham",result.getUsername());
        assertEquals(1L,result.getId());
        verify(userRepo).findById(1L);

    }
    @Test
    void shouldThorwExceptionWhenUserNotFound()
    {
        when(userRepo.findById(99L)).thenReturn(java.util.Optional.empty());

        assertThrows(userNotFoundException.class,
                ()->userService.findByid(99L));

        verify(userRepo).findById(99L);
    }

    @Test
    void shouldDeleteUserSuccessfully()
    {
        users existingUsers = new users();
        existingUsers.setId(1L);
        existingUsers.setUsername("soham");

        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(existingUsers));

        userService.deleteUser(1L);//real service method

        verify(userRepo).findById(1L);
        verify(userRepo).delete(existingUsers);
    }
    @Test
    void shouldThrowExceptionWhenDeleteUserNotFound()
    {
        when(userRepo.findById(99L)).thenReturn(java.util.Optional.empty());
        assertThrows(userNotFoundException.class,()->userService.deleteUser(99L));

        verify(userRepo).findById(99L);
        verify(userRepo,never()).delete(any(users.class));
    }
    @Test
    void shouldUpdateUserSuccessfully()
    {
        users existingUsers = new users();
        existingUsers.setId(1L);
        existingUsers.setUsername("oldname");
        existingUsers.setEmail("old@gmail.com");
        existingUsers.setFullName("old name");


        userDTO updateUser = new userDTO();
        updateUser.setUsername("newname");
        updateUser.setEmail("new@gmail.com");
        updateUser.setFullName("new name");

        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(existingUsers));

        when(userRepo.existsByUsernameAndIdNot("newname",1L)).thenReturn(false);
        when(userRepo.existsByEmailAndIdNot("new@gmail.com",1L)).thenReturn(false);

        when(userRepo.save(existingUsers)).thenReturn(existingUsers);

        users result = userService.updateUsers(1L,updateUser);
        assertNotNull(result);
        assertEquals("newname", result.getUsername());
        assertEquals("new@gmail.com", result.getEmail());
        assertEquals("new name", result.getFullName());

        verify(userRepo).findById(1L);
        verify(userRepo).existsByUsernameAndIdNot("newname", 1L);
        verify(userRepo).existsByEmailAndIdNot("new@gmail.com", 1L);
        verify(userRepo).save(existingUsers);


    }
    @Test
    void shouldRejectUpdateWhenUsernameAlreadyExist()
    {
        users existingUsers = new users();
        existingUsers.setId(1L);
        existingUsers.setUsername("oldname");

        userDTO updateData = new userDTO();
        updateData.setUsername("john");
        updateData.setEmail("new@example.com");
        updateData.setFullName("New Name");

        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(existingUsers));
        when(userRepo.existsByUsernameAndIdNot("john",1L)).thenReturn(true);

        assertThrows(duplicateUserException.class,()->userService.updateUsers(1L,updateData));

        verify(userRepo).findById(1L);
        verify(userRepo).existsByUsernameAndIdNot("john",1L);
        verify(userRepo,never()).save(any(users.class));



    }
    @Test
    void shouldRejectUpdateWhenEmailAlreadyExists()
    {
        users existingUser = new users();
        existingUser.setId(1L);
        existingUser.setUsername("oldname");

        userDTO updateData = new userDTO();
        updateData.setUsername("newname");
        updateData.setEmail("existing@example.com");
        updateData.setFullName("New Name");

        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(existingUser));
        when(userRepo.existsByUsernameAndIdNot("newname",1L)).thenReturn(false);
        when(userRepo.existsByEmailAndIdNot("existing@example.com",1L)).thenReturn(true);


        assertThrows(duplicateUserException.class,()->userService.updateUsers(1L,updateData));

        verify(userRepo).findById(1L);
        verify(userRepo).existsByEmailAndIdNot("existing@example.com", 1L);
        verify(userRepo,never()).save(any(users.class));
    }
    @Test
    void shouldGetUsersWithPagination(){
        users user = new users();
        user.setId(1L);
        user.setUsername("soham");
        user.setEmail("soham@example.com");
        user.setFullName("Soham Basak");

        Pageable pageable = PageRequest.of(0,10);

        Page<users> usersPage = new PageImpl<>(List.of(user),pageable,1);

        when(userRepo.findAll(pageable)).thenReturn(usersPage);
        Page<userResponseDTO> result = userService.getUsers(pageable);

        assertNotNull(result);
        assertEquals(1,result.getTotalElements());
        assertEquals("soham",result.getContent().get(0).getUsername());

        verify(userRepo).findAll(pageable);
    }
    @Test
    void shouldSearchUserSuccessfully() {

        users user = new users();
        user.setId(1L);
        user.setUsername("soham");
        user.setEmail("soham@example.com");
        user.setFullName("Soham Basak");

        Pageable pageable = PageRequest.of(0,10);
        Page<users> page = new PageImpl<>(List.of(user),pageable,1);

        when(userRepo.findByUsernameContainingIgnoreCase("soham",pageable)).thenReturn(page);

        Page<userResponseDTO> result = userService.searchUser(pageable,"soham");

        assertNotNull(result);
        assertEquals(1,result.getTotalElements());
        assertEquals("soham",result.getContent().get(0).getUsername());

        verify(userRepo).findByUsernameContainingIgnoreCase("soham",pageable);






    }
}



