package com.gymAdmin.controller;

import com.gymAdmin.entity.Plan;
import com.gymAdmin.repository.PlanRepository;
import com.gymAdmin.service.PlanService;
import com.gymAdmin.service.dtos.PlanRequestDto;
import com.gymAdmin.service.dtos.PlanResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planes")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @PostMapping
    public ResponseEntity<ResponseCustom>  create(@RequestBody PlanRequestDto plan) {
        return ResponseEntity.ok(
                ResponseCustom.success(planService.save(plan))
        );
    }

    @GetMapping
    public ResponseEntity<ResponseCustom>  getAllPlans() {
        return ResponseEntity.ok(ResponseCustom.success(
                planService.findAll()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCustom> getPlanById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseCustom.success(planService.findById(id)
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseCustom> updatePlan(@PathVariable Long id, @RequestBody PlanRequestDto plan) {
        return ResponseEntity.ok(
                ResponseCustom.success(
                        planService.update(plan, id)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlan(@PathVariable Long id) {
        planService.delete(id);
        return ResponseEntity.ok().build();
    }

}
