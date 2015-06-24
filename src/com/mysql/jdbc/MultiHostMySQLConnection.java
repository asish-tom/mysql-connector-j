/*
  Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.

  The MySQL Connector/J is licensed under the terms of the GPLv2
  <http://www.gnu.org/licenses/old-licenses/gpl-2.0.html>, like most MySQL Connectors.
  There are special exceptions to the terms and conditions of the GPLv2 as it is applied to
  this software, see the FLOSS License Exception
  <http://www.mysql.com/about/legal/licensing/foss-exception.html>.

  This program is free software; you can redistribute it and/or modify it under the terms
  of the GNU General Public License as published by the Free Software Foundation; version 2
  of the License.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this
  program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth
  Floor, Boston, MA 02110-1301  USA

 */

package com.mysql.jdbc;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Timer;
import java.util.concurrent.Executor;

import com.mysql.cj.api.Extension;
import com.mysql.cj.api.ProfilerEventHandler;
import com.mysql.cj.api.exception.ExceptionInterceptor;
import com.mysql.cj.api.log.Log;
import com.mysql.cj.core.Messages;
import com.mysql.cj.core.ServerVersion;
import com.mysql.cj.jdbc.JdbcConnection;
import com.mysql.cj.jdbc.JdbcPropertySet;
import com.mysql.cj.mysqla.MysqlaSession;
import com.mysql.cj.mysqla.io.Buffer;
import com.mysql.cj.mysqla.io.MysqlaProtocol;
import com.mysql.jdbc.exceptions.SQLError;
import com.mysql.jdbc.interceptors.StatementInterceptorV2;

public class MultiHostMySQLConnection implements JdbcConnection {

    protected MultiHostConnectionProxy proxy;

    public MultiHostMySQLConnection(MultiHostConnectionProxy proxy) {
        this.proxy = proxy;
    }

    public MultiHostConnectionProxy getProxy() {
        return this.proxy;
    }

    protected JdbcConnection getActiveMySQLConnection() {
        synchronized (this.proxy) {
            return this.proxy.currentConnection;
        }
    }

    public void abortInternal() throws SQLException {
        getActiveMySQLConnection().abortInternal();
    }

    public void changeUser(String userName, String newPassword) throws SQLException {
        getActiveMySQLConnection().changeUser(userName, newPassword);
    }

    public void checkClosed() {
        getActiveMySQLConnection().checkClosed();
    }

    public void clearHasTriedMaster() {
        getActiveMySQLConnection().clearHasTriedMaster();
    }

    public void clearWarnings() throws SQLException {
        getActiveMySQLConnection().clearWarnings();
    }

    public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getActiveMySQLConnection().clientPrepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getActiveMySQLConnection().clientPrepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    public PreparedStatement clientPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        return getActiveMySQLConnection().clientPrepareStatement(sql, autoGenKeyIndex);
    }

    public PreparedStatement clientPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        return getActiveMySQLConnection().clientPrepareStatement(sql, autoGenKeyIndexes);
    }

    public PreparedStatement clientPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        return getActiveMySQLConnection().clientPrepareStatement(sql, autoGenKeyColNames);
    }

    public PreparedStatement clientPrepareStatement(String sql) throws SQLException {
        return getActiveMySQLConnection().clientPrepareStatement(sql);
    }

    public void close() throws SQLException {
        getActiveMySQLConnection().close();
    }

    public void commit() throws SQLException {
        getActiveMySQLConnection().commit();
    }

    public void createNewIO(boolean isForReconnect) {
        getActiveMySQLConnection().createNewIO(isForReconnect);
    }

    public Statement createStatement() throws SQLException {
        return getActiveMySQLConnection().createStatement();
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getActiveMySQLConnection().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return getActiveMySQLConnection().createStatement(resultSetType, resultSetConcurrency);
    }

    public JdbcConnection duplicate() throws SQLException {
        return getActiveMySQLConnection().duplicate();
    }

    public ResultSetInternalMethods execSQL(StatementImpl callingStatement, String sql, int maxRows, Buffer packet, int resultSetType,
            int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata, boolean isBatch) throws SQLException {
        return getActiveMySQLConnection().execSQL(callingStatement, sql, maxRows, packet, resultSetType, resultSetConcurrency, streamResults, catalog,
                cachedMetadata, isBatch);
    }

    public ResultSetInternalMethods execSQL(StatementImpl callingStatement, String sql, int maxRows, Buffer packet, int resultSetType,
            int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata) throws SQLException {
        return getActiveMySQLConnection().execSQL(callingStatement, sql, maxRows, packet, resultSetType, resultSetConcurrency, streamResults, catalog,
                cachedMetadata);
    }

    public StringBuilder generateConnectionCommentBlock(StringBuilder buf) {
        return getActiveMySQLConnection().generateConnectionCommentBlock(buf);
    }

    public int getActiveStatementCount() {
        return getActiveMySQLConnection().getActiveStatementCount();
    }

    public boolean getAutoCommit() throws SQLException {
        return getActiveMySQLConnection().getAutoCommit();
    }

    public int getAutoIncrementIncrement() {
        return getActiveMySQLConnection().getAutoIncrementIncrement();
    }

    public CachedResultSetMetaData getCachedMetaData(String sql) {
        return getActiveMySQLConnection().getCachedMetaData(sql);
    }

    public Timer getCancelTimer() {
        return getActiveMySQLConnection().getCancelTimer();
    }

    public String getCatalog() throws SQLException {
        return getActiveMySQLConnection().getCatalog();
    }

    public String getCharacterSetMetadata() {
        return getActiveMySQLConnection().getCharacterSetMetadata();
    }

    /**
     * @deprecated replaced by <code>getEncodingForIndex(int charsetIndex)</code>
     */
    @Deprecated
    public String getCharsetNameForIndex(int charsetIndex) throws SQLException {
        return getEncodingForIndex(charsetIndex);
    }

    public String getEncodingForIndex(int collationIndex) {
        return getActiveMySQLConnection().getEncodingForIndex(collationIndex);
    }

    public TimeZone getDefaultTimeZone() {
        return getActiveMySQLConnection().getDefaultTimeZone();
    }

    public String getErrorMessageEncoding() {
        return getActiveMySQLConnection().getErrorMessageEncoding();
    }

    public ExceptionInterceptor getExceptionInterceptor() {
        return getActiveMySQLConnection().getExceptionInterceptor();
    }

    public int getHoldability() throws SQLException {
        return getActiveMySQLConnection().getHoldability();
    }

    public String getHost() {
        return getActiveMySQLConnection().getHost();
    }

    public long getId() {
        return getActiveMySQLConnection().getId();
    }

    public long getIdleFor() {
        return getActiveMySQLConnection().getIdleFor();
    }

    public MysqlaProtocol getProtocol() {
        return getActiveMySQLConnection().getProtocol();
    }

    public JdbcConnection getMultiHostSafeProxy() {
        return getActiveMySQLConnection().getMultiHostSafeProxy();
    }

    public Log getLog() {
        return getActiveMySQLConnection().getLog();
    }

    public int getMaxBytesPerChar(String javaCharsetName) {
        return getActiveMySQLConnection().getMaxBytesPerChar(javaCharsetName);
    }

    public int getMaxBytesPerChar(Integer charsetIndex, String javaCharsetName) {
        return getActiveMySQLConnection().getMaxBytesPerChar(charsetIndex, javaCharsetName);
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return getActiveMySQLConnection().getMetaData();
    }

    public Statement getMetadataSafeStatement() throws SQLException {
        return getActiveMySQLConnection().getMetadataSafeStatement();
    }

    public int getNetBufferLength() {
        return getActiveMySQLConnection().getNetBufferLength();
    }

    public Properties getProperties() {
        return getActiveMySQLConnection().getProperties();
    }

    public boolean getRequiresEscapingEncoder() {
        return getActiveMySQLConnection().getRequiresEscapingEncoder();
    }

    /**
     * @deprecated replaced by <code>getServerCharset()</code>
     */
    @Deprecated
    public String getServerCharacterEncoding() {
        return getServerCharset();
    }

    public String getServerCharset() {
        return getActiveMySQLConnection().getServerCharset();
    }

    public ServerVersion getServerVersion() {
        return getActiveMySQLConnection().getServerVersion();
    }

    public MysqlaSession getSession() {
        return getActiveMySQLConnection().getSession();
    }

    public String getStatementComment() {
        return getActiveMySQLConnection().getStatementComment();
    }

    public List<StatementInterceptorV2> getStatementInterceptorsInstances() {
        return getActiveMySQLConnection().getStatementInterceptorsInstances();
    }

    public int getTransactionIsolation() throws SQLException {
        return getActiveMySQLConnection().getTransactionIsolation();
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return getActiveMySQLConnection().getTypeMap();
    }

    public String getURL() {
        return getActiveMySQLConnection().getURL();
    }

    public String getUser() {
        return getActiveMySQLConnection().getUser();
    }

    public SQLWarning getWarnings() throws SQLException {
        return getActiveMySQLConnection().getWarnings();
    }

    public boolean hasSameProperties(JdbcConnection c) {
        return getActiveMySQLConnection().hasSameProperties(c);
    }

    public boolean hasTriedMaster() {
        return getActiveMySQLConnection().hasTriedMaster();
    }

    public void incrementNumberOfPreparedExecutes() {
        getActiveMySQLConnection().incrementNumberOfPreparedExecutes();
    }

    public void incrementNumberOfPrepares() {
        getActiveMySQLConnection().incrementNumberOfPrepares();
    }

    public void incrementNumberOfResultSetsCreated() {
        getActiveMySQLConnection().incrementNumberOfResultSetsCreated();
    }

    public void initializeExtension(Extension ex) {
        getActiveMySQLConnection().initializeExtension(ex);
    }

    public void initializeResultsMetadataFromCache(String sql, CachedResultSetMetaData cachedMetaData, ResultSetInternalMethods resultSet) throws SQLException {
        getActiveMySQLConnection().initializeResultsMetadataFromCache(sql, cachedMetaData, resultSet);
    }

    public void initializeSafeStatementInterceptors() throws SQLException {
        getActiveMySQLConnection().initializeSafeStatementInterceptors();
    }

    public boolean isAbonormallyLongQuery(long millisOrNanos) {
        return getActiveMySQLConnection().isAbonormallyLongQuery(millisOrNanos);
    }

    public boolean isInGlobalTx() {
        return getActiveMySQLConnection().isInGlobalTx();
    }

    public boolean isMasterConnection() {
        return getActiveMySQLConnection().isMasterConnection();
    }

    public boolean isNoBackslashEscapesSet() {
        return getActiveMySQLConnection().isNoBackslashEscapesSet();
    }

    public boolean isReadInfoMsgEnabled() {
        return getActiveMySQLConnection().isReadInfoMsgEnabled();
    }

    public boolean isReadOnly() throws SQLException {
        return getActiveMySQLConnection().isReadOnly();
    }

    public boolean isReadOnly(boolean useSessionStatus) throws SQLException {
        return getActiveMySQLConnection().isReadOnly(useSessionStatus);
    }

    public boolean isSameResource(JdbcConnection otherConnection) {
        return getActiveMySQLConnection().isSameResource(otherConnection);
    }

    public boolean lowerCaseTableNames() {
        return getActiveMySQLConnection().lowerCaseTableNames();
    }

    public String nativeSQL(String sql) throws SQLException {
        return getActiveMySQLConnection().nativeSQL(sql);
    }

    public void ping() throws SQLException {
        getActiveMySQLConnection().ping();
    }

    public void pingInternal(boolean checkForClosedConnection, int timeoutMillis) throws SQLException {
        getActiveMySQLConnection().pingInternal(checkForClosedConnection, timeoutMillis);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getActiveMySQLConnection().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getActiveMySQLConnection().prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        return getActiveMySQLConnection().prepareCall(sql);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getActiveMySQLConnection().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getActiveMySQLConnection().prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    public PreparedStatement prepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        return getActiveMySQLConnection().prepareStatement(sql, autoGenKeyIndex);
    }

    public PreparedStatement prepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        return getActiveMySQLConnection().prepareStatement(sql, autoGenKeyIndexes);
    }

    public PreparedStatement prepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        return getActiveMySQLConnection().prepareStatement(sql, autoGenKeyColNames);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return getActiveMySQLConnection().prepareStatement(sql);
    }

    public void realClose(boolean calledExplicitly, boolean issueRollback, boolean skipLocalTeardown, Throwable reason) throws SQLException {
        getActiveMySQLConnection().realClose(calledExplicitly, issueRollback, skipLocalTeardown, reason);
    }

    public void recachePreparedStatement(ServerPreparedStatement pstmt) throws SQLException {
        getActiveMySQLConnection().recachePreparedStatement(pstmt);
    }

    public void decachePreparedStatement(ServerPreparedStatement pstmt) throws SQLException {
        getActiveMySQLConnection().decachePreparedStatement(pstmt);
    }

    public void registerQueryExecutionTime(long queryTimeMs) {
        getActiveMySQLConnection().registerQueryExecutionTime(queryTimeMs);
    }

    public void registerStatement(com.mysql.jdbc.Statement stmt) {
        getActiveMySQLConnection().registerStatement(stmt);
    }

    public void releaseSavepoint(Savepoint arg0) throws SQLException {
        getActiveMySQLConnection().releaseSavepoint(arg0);
    }

    public void reportNumberOfTablesAccessed(int numTablesAccessed) {
        getActiveMySQLConnection().reportNumberOfTablesAccessed(numTablesAccessed);
    }

    public void reportQueryTime(long millisOrNanos) {
        getActiveMySQLConnection().reportQueryTime(millisOrNanos);
    }

    public void resetServerState() throws SQLException {
        getActiveMySQLConnection().resetServerState();
    }

    public void rollback() throws SQLException {
        getActiveMySQLConnection().rollback();
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        getActiveMySQLConnection().rollback(savepoint);
    }

    public PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getActiveMySQLConnection().serverPrepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getActiveMySQLConnection().serverPrepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    public PreparedStatement serverPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        return getActiveMySQLConnection().serverPrepareStatement(sql, autoGenKeyIndex);
    }

    public PreparedStatement serverPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        return getActiveMySQLConnection().serverPrepareStatement(sql, autoGenKeyIndexes);
    }

    public PreparedStatement serverPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        return getActiveMySQLConnection().serverPrepareStatement(sql, autoGenKeyColNames);
    }

    public PreparedStatement serverPrepareStatement(String sql) throws SQLException {
        return getActiveMySQLConnection().serverPrepareStatement(sql);
    }

    public void setAutoCommit(boolean autoCommitFlag) throws SQLException {
        getActiveMySQLConnection().setAutoCommit(autoCommitFlag);
    }

    public void setCatalog(String catalog) throws SQLException {
        getActiveMySQLConnection().setCatalog(catalog);
    }

    public void setFailedOver(boolean flag) {
        getActiveMySQLConnection().setFailedOver(flag);
    }

    public void setHoldability(int arg0) throws SQLException {
        getActiveMySQLConnection().setHoldability(arg0);
    }

    public void setInGlobalTx(boolean flag) {
        getActiveMySQLConnection().setInGlobalTx(flag);
    }

    public void setProxy(JdbcConnection proxy) {
        getActiveMySQLConnection().setProxy(proxy);
    }

    public void setReadInfoMsgEnabled(boolean flag) {
        getActiveMySQLConnection().setReadInfoMsgEnabled(flag);
    }

    public void setReadOnly(boolean readOnlyFlag) throws SQLException {
        getActiveMySQLConnection().setReadOnly(readOnlyFlag);
    }

    public void setReadOnlyInternal(boolean readOnlyFlag) throws SQLException {
        getActiveMySQLConnection().setReadOnlyInternal(readOnlyFlag);
    }

    public Savepoint setSavepoint() throws SQLException {
        return getActiveMySQLConnection().setSavepoint();
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        return getActiveMySQLConnection().setSavepoint(name);
    }

    public void setStatementComment(String comment) {
        getActiveMySQLConnection().setStatementComment(comment);
    }

    public void setTransactionIsolation(int level) throws SQLException {
        getActiveMySQLConnection().setTransactionIsolation(level);
    }

    public void shutdownServer() throws SQLException {
        getActiveMySQLConnection().shutdownServer();
    }

    public boolean storesLowerCaseTableName() {
        return getActiveMySQLConnection().storesLowerCaseTableName();
    }

    public void throwConnectionClosedException() throws SQLException {
        getActiveMySQLConnection().throwConnectionClosedException();
    }

    public void transactionBegun() throws SQLException {
        getActiveMySQLConnection().transactionBegun();
    }

    public void transactionCompleted() throws SQLException {
        getActiveMySQLConnection().transactionCompleted();
    }

    public void unregisterStatement(com.mysql.jdbc.Statement stmt) {
        getActiveMySQLConnection().unregisterStatement(stmt);
    }

    public void unSafeStatementInterceptors() throws SQLException {
        getActiveMySQLConnection().unSafeStatementInterceptors();
    }

    public boolean useAnsiQuotedIdentifiers() {
        return getActiveMySQLConnection().useAnsiQuotedIdentifiers();
    }

    public boolean versionMeetsMinimum(int major, int minor, int subminor) {
        return getActiveMySQLConnection().versionMeetsMinimum(major, minor, subminor);
    }

    public boolean isClosed() throws SQLException {
        return getActiveMySQLConnection().isClosed();
    }

    public boolean isProxySet() {
        return this.getActiveMySQLConnection().isProxySet();
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        getActiveMySQLConnection().setTypeMap(map);
    }

    public boolean isServerLocal() throws SQLException {
        return getActiveMySQLConnection().isServerLocal();
    }

    public void setSchema(String schema) throws SQLException {
        getActiveMySQLConnection().setSchema(schema);
    }

    public String getSchema() throws SQLException {
        return getActiveMySQLConnection().getSchema();
    }

    public void abort(Executor executor) throws SQLException {
        getActiveMySQLConnection().abort(executor);
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        getActiveMySQLConnection().setNetworkTimeout(executor, milliseconds);
    }

    public int getNetworkTimeout() throws SQLException {
        return getActiveMySQLConnection().getNetworkTimeout();
    }

    public Object getConnectionMutex() {
        return getActiveMySQLConnection().getConnectionMutex();
    }

    public boolean getAllowMasterDownConnections() {
        return false;
    }

    public void setAllowMasterDownConnections(boolean connectIfMasterDown) {
    }

    // TODO: we should expose here functionality available in l/b and replication connections
    public boolean getHaEnableJMX() {
        return false;
    }

    public int getSessionMaxRows() {
        return getActiveMySQLConnection().getSessionMaxRows();
    }

    public void setSessionMaxRows(int max) throws SQLException {
        getActiveMySQLConnection().setSessionMaxRows(max);
    }

    public ProfilerEventHandler getProfilerEventHandlerInstance() {
        return getActiveMySQLConnection().getProfilerEventHandlerInstance();
    }

    public void setProfilerEventHandlerInstance(ProfilerEventHandler h) {
        getActiveMySQLConnection().setProfilerEventHandlerInstance(h);
    }

    public SQLXML createSQLXML() throws SQLException {
        return getActiveMySQLConnection().createSQLXML();
    }

    public java.sql.Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return getActiveMySQLConnection().createArrayOf(typeName, elements);
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return getActiveMySQLConnection().createStruct(typeName, attributes);
    }

    public Properties getClientInfo() throws SQLException {
        return getActiveMySQLConnection().getClientInfo();
    }

    public String getClientInfo(String name) throws SQLException {
        return getActiveMySQLConnection().getClientInfo(name);
    }

    public boolean isValid(int timeout) throws SQLException {
        synchronized (this.proxy) {
            return getActiveMySQLConnection().isValid(timeout);
        }
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        getActiveMySQLConnection().setClientInfo(properties);
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        getActiveMySQLConnection().setClientInfo(name, value);
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        //checkClosed();

        // This works for classes that aren't actually wrapping anything
        return iface.isInstance(this);
    }

    public <T> T unwrap(java.lang.Class<T> iface) throws java.sql.SQLException {
        try {
            // This works for classes that aren't actually wrapping anything
            return iface.cast(this);
        } catch (ClassCastException cce) {
            throw SQLError.createSQLException(Messages.getString("Common.UnableToUnwrap", new Object[] { iface.toString() }),
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
    }

    /**
     * @throws SQLException
     * @see java.sql.Connection#createBlob()
     */
    public Blob createBlob() throws SQLException {
        return getActiveMySQLConnection().createBlob();
    }

    /**
     * @throws SQLException
     * @see java.sql.Connection#createClob()
     */
    public Clob createClob() throws SQLException {
        return getActiveMySQLConnection().createClob();
    }

    /**
     * @throws SQLException
     * @see java.sql.Connection#createNClob()
     */
    public NClob createNClob() throws SQLException {
        return getActiveMySQLConnection().createNClob();
    }

    protected ClientInfoProvider getClientInfoProviderImpl() throws SQLException {
        synchronized (this.proxy) {
            return ((ConnectionImpl) getActiveMySQLConnection()).getClientInfoProviderImpl();
        }
    }

    public String getProcessHost() {
        return getActiveMySQLConnection().getProcessHost();
    }

    @Override
    public JdbcPropertySet getPropertySet() {
        return getActiveMySQLConnection().getPropertySet();
    }
}
