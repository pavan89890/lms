package com.pavan.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.pavan.model.LeaveBO;
import com.pavan.model.LeaveDatesBO;
import com.pavan.model.LeaveTypesBO;
import com.pavan.model.UserBO;
import com.pavan.respository.LeaveDatesRepository;
import com.pavan.respository.LeaveRepository;
import com.pavan.respository.LeaveTypeRepository;
import com.pavan.respository.RoleRepository;
import com.pavan.respository.UserRepository;

@Controller
@SessionAttributes(names = "currentUser")
public class WelcomeController {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private LeaveTypeRepository leaveTypesRepository;

	@Autowired
	private LeaveRepository leaveRepository;

	@Autowired
	private LeaveDatesRepository leaveDatesRepository;

	@GetMapping("/")
	public String dashboard(Principal u, Model m) {
		UserBO currentUser = userRepository.findByUsername(u.getName());
		List<Object[]> l = leaveRepository.leaveReport(currentUser.getId());
		m.addAttribute("requested",
		               l.get(0)[0] != null ? l.get(0)[0] : 0);
		m.addAttribute("rejected",
		               l.get(0)[1] != null ? l.get(0)[1] : 0);
		m.addAttribute("approved",
		               l.get(0)[2] != null ? l.get(0)[2] : 0);
		m.addAttribute("cancelled",
		               l.get(0)[3] != null ? l.get(0)[3] : 0);
		return "dashboard";
	}

	@GetMapping("/admin/users")
	public ModelAndView employees(Model m) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("user",
		             new UserBO());
		mv.addObject("users",
		             userRepository.findAll());
		mv.addObject("roles",
		             roleRepository.findAll());
		mv.setViewName("users");
		return mv;
	}

	@PostMapping(value = "/admin/saveUser")
	@ResponseBody
	public String saveUser(UserBO user) {
		String message = "";
		user.setEnabled(true);
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()
		                                                        .trim()));
		try {
			userRepository.save(user);
			message = "SUCCESS-User saved successfully.";
		} catch (Exception e) {
			message = "ERROR-" + e.getMessage();
		}
		return message;
	}

	@GetMapping("/admin/updateUser")
	public String udpateUser(@RequestParam(value = "u") UserBO u, Model m) {
		m.addAttribute("user",
		               u == null ? new UserBO() : u);
		m.addAttribute("roles",
		               roleRepository.findAll());
		m.addAttribute("users",
		               userRepository.findAll());
		return "userPopup";
	}

	@GetMapping("/admin/deleteUser")
	public String deleteUser(@RequestParam(value = "u") UserBO u) {
		String message = "";
		try {
			userRepository.delete(u);
			return "redirect:/admin/users";
		} catch (Exception e) {
			message = "ERROR-" + e.getMessage();
			return message;
		}
	}

	@GetMapping("/login")
	public String loginPage(Model model, String error, String logout) {
		if (error != null) {
			model.addAttribute("errorMsg",
			                   "Please Enter Correct Username Or Password");
		}
		if (logout != null) {
			model.addAttribute("logoutMsg",
			                   "You have been successfully logged out");
		}
		return "login";
	}

	@GetMapping("/403")
	public String accessDeniedPage() {
		return "accessDeniedPage";
	}

	@GetMapping("/admin/leaveTypes")
	public String leaveTypes(Model m) {
		m.addAttribute("leaveTypes",
		               leaveTypesRepository.findAll());
		return "leaveTypes";
	}

	@PostMapping(value = "/admin/saveLeaveType")
	@ResponseBody
	public String saveLeaveType(LeaveTypesBO leaveType) {
		String message = "";
		try {
			leaveTypesRepository.save(leaveType);
			message = "SUCCESS-Leave Type saved successfully.";
		} catch (Exception e) {
			message = "ERROR-" + e.getMessage();
		}
		return message;
	}

	@GetMapping("/admin/updateLeaveType")
	public String updateLeaveType(@RequestParam(value = "u") LeaveTypesBO u, Model m) {
		m.addAttribute("leaveType",
		               u == null ? new LeaveTypesBO() : u);
		return "leaveTypesPopup";
	}

	@GetMapping("/admin/deleteLeaveType")
	public String deleteLeaveType(@RequestParam(value = "u") LeaveTypesBO u) {
		String message = "";
		try {
			leaveTypesRepository.delete(u);
			return "redirect:/admin/leaveTypes";
		} catch (Exception e) {
			message = "ERROR-" + e.getMessage();
			return message;
		}
	}

	@GetMapping("/user/leaveRequest")
	public String leaveRequest(Model m) {
		m.addAttribute("leaveTypes",
		               leaveTypesRepository.findAll());
		return "leaveRequest";
	}

	@PostMapping(value = "/user/saveLeaveRequest")
	@ResponseBody
	public String saveLeaveRequest(LeaveBO leave, Principal p) {
		String message = "";
		if (leave.getFromDate()
		         .after(leave.getToDate())) {
			message = "ERROR-From Date should be less or equal to To Date";
			return message;
		}
		UserBO currentUser = userRepository.findByUsername(p.getName());
		try {
			leave.setUser(currentUser);
			leave.setManager(currentUser.getManager());
			leave.setStatus("REQUESTED");

			List<Date> datesInRange = new ArrayList<>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(leave.getFromDate());

			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(leave.getToDate());

			while (calendar.before(endCalendar)) {
				Date result = calendar.getTime();
				datesInRange.add(result);
				calendar.add(Calendar.DATE,
				             1);
			}
			datesInRange.add(leave.getToDate());

			List<Date> previousLeaves = new ArrayList<Date>();
			for (LeaveDatesBO ld : currentUser.getLeaveDates()) {
				if (!ld.getLeave()
				       .getStatus()
				       .equals("CANCELLED")) {
					previousLeaves.add(ld.getLeaveDate());
				}
			}

			List<LeaveDatesBO> leaveDates = new ArrayList<LeaveDatesBO>();
			for (Date d : datesInRange) {
				if (previousLeaves.contains(d)) {
					Calendar c = Calendar.getInstance();
					c.setTime(d);
					message = "ERROR-You have already taken leave on " + c.get(Calendar.DATE) + "/"
					        + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
					return message;
				}
				LeaveDatesBO leaveDate = new LeaveDatesBO();
				leaveDate.setLeave(leave);
				leaveDate.setLeaveDate(d);
				leaveDate.setUser(currentUser);
				leaveDates.add(leaveDate);
				leaveDatesRepository.save(leaveDate);
			}

			leave.setLeaveDates(leaveDates);
			leaveRepository.save(leave);
			message = "SUCCESS-Leave Request Saved Successfully.";
		} catch (Exception e) {
			message = "ERROR-" + e.getMessage();
		}
		return message;
	}

	@GetMapping("/user/leaveCancel")
	public String leaveCancel(Model m, Principal u) {
		UserBO currentUser = userRepository.findByUsername(u.getName());
		m.addAttribute("leaves",
		               currentUser.getLeaves());
		return "leaveCancel";
	}

	@GetMapping("/user/saveLeaveCancel")
	public String saveLeaveCancel(@RequestParam(value = "leave") LeaveBO leave) {
		String message = "";
		try {
			leave.setStatus("CANCELLED");
			leaveRepository.save(leave);
			return "redirect:/user/leaveCancel";
		} catch (Exception e) {
			message = "ERROR-" + e.getMessage();
			return message;
		}
	}

	@GetMapping("/user/leaveApprove")
	public String leaveApprove(Model m, Principal u) {
		UserBO currentUser = userRepository.findByUsername(u.getName());
		m.addAttribute("leaves",
		               leaveRepository.findByManagerAndStatus(currentUser,
		                                                      "REQUESTED"));
		return "leaveApprove";
	}

	@GetMapping("/user/saveLeaveApprove")
	public String saveLeaveApprove(@RequestParam(value = "leave") LeaveBO leave, String status) {
		String message = "";
		try {
			leave.setStatus(status);
			leaveRepository.save(leave);
			return "redirect:/user/leaveApprove";
		} catch (Exception e) {
			message = "ERROR-" + e.getMessage();
			return message;
		}
	}

	@GetMapping("/user/leaveReport")
	public String leaveReport(Model m, Principal u,
	        @RequestParam(required = false, defaultValue = "", name = "status") String status) {
		UserBO currentUser = userRepository.findByUsername(u.getName());
		if (status.isEmpty()) {
			m.addAttribute("leaves",
			               currentUser.getLeaves());
		} else {
			m.addAttribute("leaves",
			               leaveRepository.findByUserAndStatus(currentUser,
			                                                   status));
		}
		return "leaveReport";
	}

	@GetMapping("/user/leaveReportInner")
	public String leaveReportInner(Model m, Principal u,
	        @RequestParam(required = false, defaultValue = "", name = "status") String status) {
		UserBO currentUser = userRepository.findByUsername(u.getName());
		if (status.isEmpty()) {
			m.addAttribute("leaves",
			               currentUser.getLeaves());
		} else {
			m.addAttribute("leaves",
			               leaveRepository.findByUserAndStatus(currentUser,
			                                                   status));
		}
		return "leaveReportInner";
	}
}
