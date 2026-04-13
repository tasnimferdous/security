package com.project.security.service;


import com.project.security.entity.Information;

import java.util.List;

public interface InformationService {
    Information saveData(Information information);
    List<Information> getAll();
}
