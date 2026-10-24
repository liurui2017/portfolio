/**
 * 业务逻辑层。
 *
 * <p>服务保持职责单一，事务边界定义在这里（{@code @Service} + {@code @Transactional}），
 * 查询方法使用 {@code @Transactional(readOnly = true)}。</p>
 */
package org.example.portfolioshowcase.service;
