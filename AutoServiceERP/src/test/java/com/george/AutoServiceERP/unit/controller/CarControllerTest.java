package com.george.AutoServiceERP.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.george.AutoServiceERP.exception.CarAlreadyExistsException;
import com.george.AutoServiceERP.exception.GlobalExceptionHandler;
import com.george.AutoServiceERP.model.Car;
import com.george.AutoServiceERP.service.CarService;
import com.george.AutoServiceERP.controller.CarController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CarControllerTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(carController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Замените YourGlobalExceptionHandler на класс вашего глобального обработчика исключений
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddCar() throws Exception {
        Car car = new Car();
        car.setNum("XYZ 123");
        car.setColor("Синий");
        car.setMark("Toyota");
        car.setForeign(true);
        when(carService.saveCar(any(Car.class))).thenReturn(car);

        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.num").value("XYZ 123"));

        verify(carService, times(1)).saveCar(any(Car.class));
    }

    @Test
    void testAddExistingCar() throws Exception {
        Car car = new Car();
        car.setNum("XYZ 123");
        car.setColor("Синий");
        car.setMark("Toyota");
        car.setForeign(true);

        // Мокаем поведение сервиса при добавлении существующего автомобиля
        when(carService.saveCar(any(Car.class))).thenThrow(new CarAlreadyExistsException("Car with number " + car.getNum() + " already exists"));

        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Car with number " + car.getNum() + " already exists"));

        verify(carService, times(1)).saveCar(any(Car.class));
    }

    @Test
    void testGetAllCars() throws Exception {
        List<Car> cars = Arrays.asList(new Car(), new Car());
        when(carService.getAllCars()).thenReturn(cars);

        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(cars.size()));

        verify(carService, times(1)).getAllCars();
    }

    @Test
    void testUpdateCar() throws Exception {
        Long carId = 1L;
        Car existingCar = new Car();
        existingCar.setNum("XYZ 123");
        existingCar.setColor("Синий");
        existingCar.setMark("Toyota");
        existingCar.setForeign(true);

        Car updatedCar = new Car();
        updatedCar.setNum("XYZ 456");
        updatedCar.setColor("Красный");
        updatedCar.setMark("Toyota");
        updatedCar.setForeign(true);

        when(carService.updateCar(eq(carId), any(Car.class))).thenReturn(updatedCar);

        mockMvc.perform(put("/api/cars/" + carId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCar)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.num").value("XYZ 456"));

        verify(carService, times(1)).updateCar(eq(carId), any(Car.class));
    }

    @Test
    void testDeleteCar() throws Exception {
        Long carId = 1L;
        when(carService.deleteCar(carId)).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/api/cars/" + carId))
                .andExpect(status().isOk());

        verify(carService, times(1)).deleteCar(carId);
    }

}
