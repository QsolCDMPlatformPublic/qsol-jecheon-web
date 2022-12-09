package qsol.qsoljecheonweb.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

	@RequestMapping("/")
	public String view(ModelMap modelMap) {
		return "/view/mobileWeb/security/Login";
	}

	@RequestMapping("/view/{firstStep}/{secondStep}")
	public String view(@PathVariable String firstStep, @PathVariable String secondStep, ModelMap modelMap) {
		return "view/" + firstStep + "/" + secondStep;
	}


	@RequestMapping("/view/{firstStep}/{secondStep}/{thirdStep}")
	public String view(@PathVariable String firstStep, @PathVariable String secondStep,
			@PathVariable String thirdStep, ModelMap modelMap) {
		if ("view".equals(secondStep) || "update".equals(secondStep)) {
			modelMap.addAttribute("id", thirdStep);
			return "view/" + firstStep + "/" + secondStep;
		}

		return "view/" + firstStep + "/" + secondStep + "/" + thirdStep;
	}

	@RequestMapping("/view/{firstStep}/{secondStep}/{thirdStep}/{forthStep}")
	public String view(@PathVariable String firstStep, @PathVariable String secondStep,
			@PathVariable String thirdStep, @PathVariable String forthStep, ModelMap modelMap) {
		if ("view".equals(thirdStep) || "update".equals(thirdStep)) {
			modelMap.addAttribute("id", forthStep);
			return "view/" + firstStep + "/" + secondStep + "/" + thirdStep;
		}

		return "view/" + firstStep + "/" + secondStep + "/" + thirdStep + "/" + forthStep;
	}

	@RequestMapping("/view/{firstStep}/{secondStep}/{thirdStep}/{forthStep}/{fifthStep}")
	public String view(@PathVariable String firstStep, @PathVariable String secondStep,
			@PathVariable String thirdStep, @PathVariable String forthStep, @PathVariable String fifthStep, ModelMap modelMap) {
		if ("view".equals(forthStep) || "update".equals(forthStep)) {
			modelMap.addAttribute("id", fifthStep);
			return "view/" + firstStep + "/" + secondStep + "/" + thirdStep + "/" + forthStep;
		}
		return "view/" + firstStep + "/" + secondStep + "/" + thirdStep + "/" + forthStep + "/" + fifthStep;
	}
}
