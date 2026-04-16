package cn.iocoder.yudao.module.bpm.service.message;

import cn.iocoder.yudao.framework.web.config.WebProperties;
import cn.iocoder.yudao.module.bpm.service.message.dto.BpmMessageSendWhenProcessInstanceApproveReqDTO;
import cn.iocoder.yudao.module.bpm.service.message.dto.BpmMessageSendWhenProcessInstanceRejectReqDTO;
import cn.iocoder.yudao.module.bpm.service.message.dto.BpmMessageSendWhenTaskCreatedReqDTO;
import cn.iocoder.yudao.module.bpm.service.message.dto.BpmMessageSendWhenTaskTimeoutReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * BPM 消息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class BpmMessageServiceImpl implements BpmMessageService {

    @Resource
    private WebProperties webProperties;

    @Override
    public void sendMessageWhenProcessInstanceApprove(BpmMessageSendWhenProcessInstanceApproveReqDTO reqDTO) {
        // 短信功能已移除，仅记录日志
        log.info("流程实例[{}]已通过，申请人ID[{}]", reqDTO.getProcessInstanceName(), reqDTO.getStartUserId());
    }

    @Override
    public void sendMessageWhenProcessInstanceReject(BpmMessageSendWhenProcessInstanceRejectReqDTO reqDTO) {
        // 短信功能已移除，仅记录日志
        log.info("流程实例[{}]已拒绝，申请人ID[{}]，原因：{}", reqDTO.getProcessInstanceName(), reqDTO.getStartUserId(), reqDTO.getReason());
    }

    @Override
    public void sendMessageWhenTaskAssigned(BpmMessageSendWhenTaskCreatedReqDTO reqDTO) {
        // 短信功能已移除，仅记录日志
        log.info("任务[{}]已分配，流程实例[{}]，审批人ID[{}]", reqDTO.getTaskName(), reqDTO.getProcessInstanceName(), reqDTO.getAssigneeUserId());
    }

    @Override
    public void sendMessageWhenTaskTimeout(BpmMessageSendWhenTaskTimeoutReqDTO reqDTO) {
        // 短信功能已移除，仅记录日志
        log.info("任务[{}]已超时，流程实例[{}]，审批人ID[{}]", reqDTO.getTaskName(), reqDTO.getProcessInstanceName(), reqDTO.getAssigneeUserId());
    }

    private String getProcessInstanceDetailUrl(String taskId) {
        return webProperties.getAdminUi().getUrl() + "/bpm/process-instance/detail?id=" + taskId;
    }

}
