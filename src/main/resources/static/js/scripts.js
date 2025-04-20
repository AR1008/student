// Common JavaScript functions for Student Attendance Management System

document.addEventListener('DOMContentLoaded', function() {
    // Initialize Bootstrap tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Initialize Bootstrap popovers
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
    
    // Attendance marking functions
    initAttendanceMarking();
    
    // Biometric simulation
    initBiometricSimulation();
    
    // Filter functionality for tables
    initTableFilters();
});

// Attendance marking functions
function initAttendanceMarking() {
    // Get all attendance status selectors
    const statusSelectors = document.querySelectorAll('.attendance-status-select');
    
    if (statusSelectors.length === 0) return;
    
    // Add change event listener to each selector
    statusSelectors.forEach(selector => {
        selector.addEventListener('change', function() {
            // Highlight the selected status
            const row = this.closest('tr');
            row.className = ''; // Clear existing classes// Common JavaScript functions for Student Attendance Management System

            document.addEventListener('DOMContentLoaded', function() {
                // Initialize Bootstrap tooltips
                var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
                var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                    return new bootstrap.Tooltip(tooltipTriggerEl);
                });
                
                // Initialize Bootstrap popovers
                var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
                var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
                    return new bootstrap.Popover(popoverTriggerEl);
                });
                
                // Attendance marking functions
                initAttendanceMarking();
                
                // Biometric simulation
                initBiometricSimulation();
                
                // Filter functionality for tables
                initTableFilters();
            });
            
            // Attendance marking functions
            function initAttendanceMarking() {
                // Get all attendance status selectors
                const statusSelectors = document.querySelectorAll('.attendance-status-select');
                
                if (statusSelectors.length === 0) return;
                
                // Add change event listener to each selector
                statusSelectors.forEach(selector => {
                    selector.addEventListener('change', function() {
                        // Highlight the selected status
                        const row = this.closest('tr');
                        row.className = ''; // Clear existing classes