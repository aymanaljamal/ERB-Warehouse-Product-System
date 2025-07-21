package com.erb.demo.testapi;

import com.erb.demo.dto.EmployeeDto;
import com.erb.demo.mapper.EmployeeMapper;
import com.erb.demo.model.Employee;
import com.erb.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestEmployeeServiceImpl implements TestEmployeeService {

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private EmployeeMapper mapper;

    @Override
    public List<EmployeeDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto getById(Long id) {
        Employee employee = repository.findById(id).orElse(null);
        return mapper.toDto(employee);
    }

    @Override
    public EmployeeDto save(EmployeeDto dto) {
        Employee entity = mapper.toEntity(dto);
        Employee saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
