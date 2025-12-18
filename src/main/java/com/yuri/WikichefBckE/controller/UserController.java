package com.yuri.WikichefBckE.controller;


import com.yuri.WikichefBckE.Service.UserService;
import com.yuri.WikichefBckE.dto.UserDTO;
import com.yuri.WikichefBckE.exception.NoEncontradoException;
import com.yuri.WikichefBckE.modelo.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Autowired
private UserService userService;
@Autowired
private ModelMapper modelMapper;
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {


        private final UserService userService;

        /**
         * Crear usuario
         */
        @PostMapping
        public ResponseEntity<UserDTO> crearUsuario(
                @RequestBody UserDTO userDTO) {
            UserDTO creado = UserService.crear(userDTO);
            return ResponseEntity.ok(creado);
        }

        /**
         * Listar usuarios
         */
        @GetMapping
        public ResponseEntity<List<UserDTO>> listarUsuarios() {
            return ResponseEntity.ok(UserService.listar());
        }

        /**
         * Obtener usuario por ID
         */
        @GetMapping("/{id}")
        public ResponseEntity<UserDTO> obtenerUsuario(
                @PathVariable Integer id) {
            ResponseEntity<UserDTO> ok = ResponseEntity.ok(UserService.obtenerPorId(id));
            return ok;
        }

        /**
         * Actualizar usuario
         */
        @PutMapping("/{id}")
        public ResponseEntity<UserDTO> actualizarUsuario(
                @PathVariable Integer id,
                @RequestBody UserDTO userDTO) {
            return ResponseEntity.ok(
                    UserService.actualizar(id, userDTO)
            );
        }

        /**
         * Eliminar usuario
         */
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminarUsuario(
                @PathVariable Integer id) {
            UserService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
    }
    /*@GetMapping
    public ResponseEntity<List<UserDTO>> obtenerTodosLosPuestosTrabajo(@PathVariable Integer id) {
        List<UserDTO> UserDTO = UserService.obtenerTodosLosPuestosTrabajo()
                .stream()
                .map(puesto -> modelMapper.map(puesto, UserDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(puestosTrabajoDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> obtenerUnPuestoTrabajo(@PathVariable int id) {
        User users = UserService.obtenerPuestoTrabajoPorId(id);
        if (puestosTrabajo == null) {
            throw new NoEncontradoException();
        }

        return new ResponseEntity(modelMapper.map(puestosTrabajo, UserDTO.class), HttpStatus.OK);
    }
    @GetMapping("/hateos/{id}")
    public EntityModel<UserDTO> consultarUnoH(@PathVariable("id") int id) {
        PuestosTrabajo puestosTrabajo = puestoTrabajoService.obtenerPuestoTrabajoPorId(id);
        if (puestosTrabajo == null){
            throw new NoEncontradoException();
        }
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).obtenerUnPuestoTrabajo(id));
        return EntityModel.of(modelMapper.map(puestosTrabajo,UserDTO.class)).add(link1.withRel("puestos_trabajo-link"));
    }*/
    /*@PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        User userBBDD = userService.createUser(user);
        return new ResponseEntity<>(modelMapper.map(userBBDD, UserDTO.class), HttpStatus.CREATED);

    }*/
    /*
    @PutMapping
    public ResponseEntity<UserDTO> modificarPuestoTrabajo(@RequestBody UserDTO puestoTrabajoDTO) {
        PuestosTrabajo puestosTrabajoBBDD = UserService.obtenerPuestoTrabajoPorId(puestoTrabajoDTO.getId());
        if (puestosTrabajoBBDD == null) {
            throw new NoEncontradoException();
        }
        puestosTrabajoBBDD = UserService.actualizarPuestoTrabajo(modelMapper.map(puestoTrabajoDTO, User.class));
        return new ResponseEntity<>(modelMapper.map(puestosTrabajoBBDD, UserDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/id")
    public ResponseEntity<Void> eliminarPuestoTrabajoPorId(@PathVariable Integer id) {
         puestosTrabajoBBDD = UserService.obtenerId(id);
        if (puestosTrabajoBBDD == null) {
            throw new NoEncontradoException();
        }
        UserService.eliminarPuestoTrabajo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }*/






