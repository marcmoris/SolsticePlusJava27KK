require('dotenv').config();
const sql = require('mssql');

const dbConfig = {
  server: process.env.DB_SERVER || 'localhost',
  port: parseInt(process.env.DB_PORT, 10) || 51259,
  database: process.env.DB_NAME || 'free-sql-db-9730850',
  user: process.env.DB_USER || 'sa',
  password: process.env.DB_PASSWORD || 'SolsMx001$!',
  options: {
    encrypt: process.env.DB_ENCRYPT === 'true',
    trustServerCertificate: process.env.DB_TRUST_SERVER_CERT !== 'false',
    enableArithAbort: true,
    requestTimeout: 30000,
  },
  pool: {
    max: 15,
    min: 0,
    idleTimeoutMillis: 30000,
  },
};

let poolPromise = null;

async function getPool() {
  if (!poolPromise) {
    poolPromise = new sql.ConnectionPool(dbConfig)
      .connect()
      .then((pool) => {
        console.log(`[SQL Server] Connecté avec succès à ${dbConfig.server}:${dbConfig.port}/${dbConfig.database}`);
        return pool;
      })
      .catch((err) => {
        console.error('[SQL Server] Erreur de connexion:', err.message);
        poolPromise = null;
        throw err;
      });
  }
  return poolPromise;
}

async function query(sqlText, params = {}) {
  const pool = await getPool();
  const request = pool.request();
  for (const [key, value] of Object.entries(params)) {
    request.input(key, value);
  }
  return request.query(sqlText);
}

async function testConnection() {
  try {
    const res = await query('SELECT 1 AS connected, @@VERSION AS version, DB_NAME() AS currentDb');
    return {
      success: true,
      currentDb: res.recordset[0].currentDb,
      version: res.recordset[0].version.split('\n')[0],
      server: `${dbConfig.server}:${dbConfig.port}`,
    };
  } catch (err) {
    return {
      success: false,
      error: err.message,
      server: `${dbConfig.server}:${dbConfig.port}`,
    };
  }
}

module.exports = {
  sql,
  getPool,
  query,
  testConnection,
};
