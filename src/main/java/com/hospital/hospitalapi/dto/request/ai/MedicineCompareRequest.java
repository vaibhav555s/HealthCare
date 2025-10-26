package com.hospital.hospitalapi.dto.request.ai;

import lombok.Data;
import java.util.List;

@Data
public class MedicineCompareRequest {
    private List<String> medicines;
}
